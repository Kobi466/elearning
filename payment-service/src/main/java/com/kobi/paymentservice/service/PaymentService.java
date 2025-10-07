package com.kobi.paymentservice.service;

import com.kobi.paymentservice.config.VnPayConfig;
import com.kobi.paymentservice.dto.request.CreatePaymentResponse;
import com.kobi.paymentservice.dto.response.CreatePaymentRequest;
import com.kobi.paymentservice.entity.Order;
import com.kobi.paymentservice.exception.AppException;
import com.kobi.paymentservice.exception.ErrorCode;
import com.kobi.paymentservice.repository.PaymentRepository;
import com.kobi.paymentservice.repository.httpClient.CourseClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    PaymentRepository paymentRepository;
    CourseClient courseClient;
    VnPayConfig vnPayConfig;

    public CreatePaymentResponse createPayment(CreatePaymentRequest createPaymentRequest, HttpServletRequest httpServletRequest) {
        var course = courseClient.getCoursePrice(createPaymentRequest.getCourseId());
        var price = course.block().getData().getPrice();
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
    private String getUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
