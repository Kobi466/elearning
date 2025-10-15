package com.kobi.paymentservice.service;

import com.kobi.paymentservice.config.VnPayConfig;
import com.kobi.paymentservice.dto.request.CreatePaymentResponse;
import com.kobi.paymentservice.dto.response.CreatePaymentRequest;
import com.kobi.paymentservice.dto.response.VnPayIPNResponse;
import com.kobi.paymentservice.entity.Order;
import com.kobi.paymentservice.entity.enums.OrderStatus;
import com.kobi.paymentservice.exception.AppException;
import com.kobi.paymentservice.exception.ErrorCode;
import com.kobi.paymentservice.repository.PaymentRepository;
import com.kobi.paymentservice.repository.httpClient.CourseClient;
import com.kobi.paymentservice.repository.httpClient.VnPayClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VnPayService {
    PaymentRepository paymentRepository;
    CourseClient courseClient;
    VnPayConfig vnPayConfig;
    OutboxEventService outboxEventService;
    VnPayClient vnPayClient;

    @Transactional
    public CreatePaymentResponse createPayment(CreatePaymentRequest createPaymentRequest, HttpServletRequest httpServletRequest) {
        var course = courseClient.getCoursePrice(createPaymentRequest.getCourseId());
        var price = Objects.requireNonNull(course.block()).getData().getPrice();
        if (course == null || price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        var order = Order.builder()
                .userId(getUserId())
                .courseId(createPaymentRequest.getCourseId())
                .amount(price)
                .orderRef(UUID.randomUUID().toString())
                .paymentGateway("VNPay")
                .build();
        paymentRepository.save(order);
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(price.multiply(new BigDecimal(100)).longValue()));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", order.getOrderRef());
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang " + order.getOrderRef());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", httpServletRequest.getRemoteAddr());

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));

        String queryUrl = VnPayUtils.getQueryUrl(vnp_Params, vnPayConfig.getHashSecret());
        String paymentUrl = vnPayConfig.getUrl() + "?" + queryUrl;

        return CreatePaymentResponse.builder()
                .paymentUrl(paymentUrl)
                .build();
    }

    //best pratice là sẽ được vnpay gọi đến api này và trả lại vnpay trạng thái thành công hay không
    //Luồng 2: Xử lý qua IPN (Server-Side)
    //Luồng này là một cuộc gọi trực tiếp giữa hai server, đáng tin cậy hơn.
    //
    //Thanh toán: Người dùng hoàn tất thanh toán.
    //
    //Gọi IPN: Server VNPAY chủ động gọi trực tiếp đến một địa chỉ public (ví dụ: qua ngrok) của Back-end bạn.
    //
    //Xác thực: Back-end của bạn nhận cuộc gọi, xác thực và xử lý.
    //
    //Cập nhật DB: Back-end cập nhật trạng thái vào Database.
    @Transactional
    public VnPayIPNResponse handleVnPayIPN(Map<String, String> allRequestParams){
        String vnp_SecureHash = allRequestParams.remove("vnp_SecureHash");
        // 1. Xác thực chữ ký
        String calculatedHash = VnPayUtils.hmacSHA512(vnPayConfig.getHashSecret(), VnPayUtils.getHashData(allRequestParams));
        if (!calculatedHash.equals(vnp_SecureHash)) {
            return new VnPayIPNResponse("02", "Invalid Signature");
        }
        String vnp_TxnRef =allRequestParams.get("vnp_TxnRef");
        var order = paymentRepository.findByOrderRef(vnp_TxnRef).orElse(null);
        // 2. Kiểm tra đơn hàng
        if (order == null) {
            return new VnPayIPNResponse("01", "Order not found");
        }
        // 3. Kiểm tra trạng thái đơn hàng
        if (!OrderStatus.PENDING.equals(order.getStatus())) {
            return new VnPayIPNResponse("02", "Order already confirmed");
        }
        // 4. Kiểm tra số tiền
        long requestAmount = Long.parseLong(allRequestParams.get("vnp_Amount"));
        long orderAmount = order.getAmount().multiply(new BigDecimal(100)).longValue();
        if (requestAmount != orderAmount) {
            return new VnPayIPNResponse("04", "Invalid amount");
        }
        // 5. Cập nhật trạng thái đơn hàng và ghi danh
        String vnp_ResponseCode = allRequestParams.get("vnp_ResponseCode");
        if ("00".equals(vnp_ResponseCode)) {
            order.setStatus(OrderStatus.PAID);
            order.setPayDate(allRequestParams.get("vnp_PayDate"));
            order.setGatewayTransId(allRequestParams.get("vnp_TransactionNo"));
            paymentRepository.save(order);
            // enrollment
            outboxEventService.createAndPublishOutboxEvent(order, "ORDER_PAID");
            return new VnPayIPNResponse("00", "Transaction successful");
        } else {
            order.setStatus(OrderStatus.FAILED);
            paymentRepository.save(order);
            return  new VnPayIPNResponse("99", "Transaction failed");
        }
    }

    //Luồng 1: Xử lý qua returnUrl (Client-Side)
    //Luồng này phụ thuộc vào trình duyệt của người dùng để mang thông tin về.
    //
    //Thanh toán: Người dùng hoàn tất thanh toán trên trang VNPAY.
    //
    //Chuyển hướng: Server VNPAY ra lệnh cho trình duyệt của người dùng đi về returnUrl.
    //
    //Gọi Back-end: Trình duyệt truy cập vào Back-end của bạn (ví dụ: localhost:8085) và gửi kèm kết quả thanh toán.
    //
    //Cập nhật DB: Back-end xác thực và cập nhật Database.
//    @Transactional
//    public String handleVnPayReturn(Map<String, String> allRequestParams) {
//        log.info("1. Bắt đầu xử lý VNPAY return với các tham số:{}",  allRequestParams);
//        //thay vào địa chỉ cảu FE
//        String feSuccessUrl = "https://google.com?payment_status=success";
//        String feFailUrl = "https://google.com?payment_status=fail";
//
//        String vnp_SecureHash = allRequestParams.get("vnp_SecureHash");
//        if (vnp_SecureHash == null) {
//            log.error("2. Lỗi: Không có vnp_SecureHash!");
//            return feFailUrl;
//        }
//
//        // Tạo một Map mới chỉ chứa các tham số sẽ được dùng để hash
//        Map<String, String> fieldsToHash = new HashMap<>();
//        for (Map.Entry<String, String> entry : allRequestParams.entrySet()) {
//            String fieldName = entry.getKey();
//            String fieldValue = entry.getValue();
//            if (fieldName.startsWith("vnp_") && !fieldName.equals("vnp_SecureHash")) {
//                fieldsToHash.put(fieldName, fieldValue);
//            }
//        }
//
//        String calculatedHash = VnPayUtils.hmacSHA512(vnPayConfig.getHashSecret(), VnPayUtils.getHashData(fieldsToHash));
//
//        if (!calculatedHash.equals(vnp_SecureHash)) {
//            log.error("2. Lỗi: Chữ ký không hợp lệ!");
//            log.error("   - Chữ ký VNPAY trả về: {}" , vnp_SecureHash);
//            log.error("   - Chữ ký tính toán lại: {}", calculatedHash);
//            return feFailUrl;
//        }
//
//        String vnp_TxnRef = allRequestParams.get("vnp_TxnRef");
//        Order order = paymentRepository.findByOrderRef(vnp_TxnRef).orElse(null);
//
//        if (order == null) {
//            log.error("3. Lỗi: Không tìm thấy đơn hàng với orderRef: {}", vnp_TxnRef);
//            return feFailUrl;
//        }
//
//        if (!OrderStatus.PENDING.equals(order.getStatus())) {
//            log.error("4. Lỗi: Đơn hàng không ở trạng thái PENDING. Trạng thái hiện tại:{}", order.getStatus());
//            return feFailUrl;
//        }
//
//        long requestAmount = Long.parseLong(allRequestParams.get("vnp_Amount"));
//        long orderAmount = order.getAmount().multiply(new BigDecimal(100)).longValue();
//        if (requestAmount != orderAmount) {
//            log.error("5. Lỗi: Số tiền không khớp. VNPAY trả về:{}, Đơn hàng lưu:{}", requestAmount, orderAmount);
//            return feFailUrl;
//        }
//
//        String vnp_ResponseCode = allRequestParams.get("vnp_ResponseCode");
//        if ("00".equals(vnp_ResponseCode)) {
//            log.error("6. Thành công! Cập nhật trạng thái đơn hàng.");
//            order.setStatus(OrderStatus.PAID);
//            order.setPayDate(allRequestParams.get("vnp_PayDate"));
//            order.setGatewayTransId(allRequestParams.get("vnp_TransactionNo"));
//            paymentRepository.save(order);
//            // 2. GỌI OUTBOX SERVICE ĐỂ TẠO SỰ KIỆN
//            outboxEventService.createAndPublishOutboxEvent(order, "ORDER_PAID");
//
//            return feSuccessUrl;
//        } else {
//            log.error("7. Thất bại từ VNPAY. ResponseCode: {}", vnp_ResponseCode);
//            order.setStatus(OrderStatus.FAILED);
//            paymentRepository.save(order);
//            return feFailUrl;
//        }
//    }

    @PreAuthorize("hasRole('ADMIN')")
    public void refundOrder(String orderId) {
        log.info("Bắt đầu quy trình hoàn tiền cho orderId: {}", orderId);
        Order order = paymentRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.getStatus().equals(OrderStatus.FAILED)) {
            throw new IllegalStateException("Chỉ có thể hoàn tiền cho đơn hàng ghi danh thất bại. Trạng thái hiện tại: " + order.getStatus());
        }

        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_RequestId", UUID.randomUUID().toString());
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "refund");
        vnp_Params.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnp_Params.put("vnp_TransactionType", "02");
        vnp_Params.put("vnp_TxnRef", order.getOrderRef());
        vnp_Params.put("vnp_Amount", String.valueOf(order.getAmount().multiply(new BigDecimal(100)).longValue()));
        vnp_Params.put("vnp_OrderInfo", "Hoan tien cho don hang " + order.getOrderRef());
        vnp_Params.put("vnp_TransactionNo", order.getGatewayTransId());
        vnp_Params.put("vnp_TransactionDate", order.getPayDate());
        vnp_Params.put("vnp_CreateBy", "admin_system");
        String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        vnp_Params.put("vnp_CreateDate", createDate);
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");//local
        log.info("FINAL PAYLOAD TO SEND: {}", vnp_Params);
        //process
        String hashData = VnPayUtils.getPipeDelimitedHashData(vnp_Params);
        log.info("Chuỗi hashData đã sửa: {}", hashData);
        String calculatedHash = VnPayUtils.hmacSHA512(vnPayConfig.getHashSecret(), hashData);
        log.info("Chuỗi hashData đã sửa: {}", calculatedHash);
        vnp_Params.put("vnp_SecureHash", calculatedHash);

        // call VnPay
        var response = vnPayClient.callRefund(vnp_Params).block();
        if (response != null && "00".equals(response.getRspCode())) {
            order.setStatus(OrderStatus.REFUND);
            paymentRepository.save(order);
            log.info("Hoàn tiền thành công cho orderId: {}", orderId);
        } else {
            String message = response != null ? response.getMessage() : "Response is null";
            String rspCode = response != null ? response.getRspCode() : "N/A";
            log.error("Hoàn tiền thất bại từ VNPAY. Mã lỗi: {}, Thông điệp: {}", rspCode, message);
            throw new RuntimeException("Hoàn tiền thất bại: " + message);
        }
    }
    private String getUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
