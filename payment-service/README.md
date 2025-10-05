# Payment Service

This service is responsible for handling payments in the Kobi E-learning platform.

## VNPay Integration

We are currently integrating VNPay into our payment service. This will allow users to pay for courses using the VNPay payment gateway.

### Luồng hoạt động (Workflow)

1.  **Khởi tạo thanh toán (Initiate Payment):**
    *   Người dùng chọn thanh toán bằng VNPay trên giao diện (UI).
    *   UI gửi yêu cầu đến `payment-service` để tạo một đơn hàng thanh toán mới.

2.  **Tạo URL thanh toán (Create Payment URL):**
    *   `payment-service` nhận yêu cầu, tạo một bản ghi giao dịch trong cơ sở dữ liệu với trạng thái "chờ thanh toán" (pending).
    *   Service gọi đến API của VNPay, gửi thông tin đơn hàng (số tiền, mã đơn hàng, v.v.).
    *   VNPay xử lý và trả về một URL thanh toán duy nhất cho giao dịch này.
    *   `payment-service` trả URL này về cho UI.

3.  **Chuyển hướng đến cổng VNPay (Redirect to VNPay Gateway):**
    *   UI nhận được URL và chuyển hướng người dùng đến trang thanh toán của VNPay.

4.  **Xử lý thanh toán (Process Payment):**
    *   Người dùng nhập thông tin thanh toán (thẻ, tài khoản ngân hàng) trên cổng VNPay và xác nhận.

5.  **VNPay gọi IPN (Instant Payment Notification):**
    *   Sau khi người dùng hoàn tất thanh toán, VNPay sẽ gửi một yêu cầu (callback) đến một endpoint đặc biệt trên `payment-service` (được gọi là IPN URL).
    *   Yêu cầu này chứa kết quả của giao dịch (thành công hay thất bại) và chữ ký số để xác thực.

6.  **Xác thực và cập nhật giao dịch (Verify and Update Transaction):**
    *   `payment-service` nhận được IPN, tiến hành xác thực chữ ký số của VNPay để đảm bảo dữ liệu không bị giả mạo.
    *   Service gọi lại API của VNPay để một lần nữa truy vấn trạng thái của giao dịch (để tăng cường bảo mật).
    *   Nếu giao dịch hợp lệ và thành công, `payment-service` sẽ cập nhật trạng thái đơn hàng trong cơ sở dữ liệu thành "đã thanh toán" (completed). Đồng thời, có thể kích hoạt các logic nghiệp vụ khác (ví dụ: cấp quyền truy cập khóa học cho người dùng).

7.  **Người dùng quay lại trang web (User Return):**
    *   Sau khi thanh toán xong trên cổng VNPay, người dùng sẽ được chuyển hướng trở lại trang web của Kobi Elearning (Return URL).
    *   UI hiển thị thông báo thanh toán thành công hoặc thất bại dựa trên kết quả trả về từ VNPay.

### TODO

- [ ] Implement the VNPay payment gateway
- [ ] Add a new endpoint for creating VNPay payments
- [ ] Add an IPN endpoint to handle VNPay callbacks
- [ ] Update the UI to support VNPay payments
