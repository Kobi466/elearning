# Dịch vụ thanh toán (Payment Service)

Dịch vụ thanh toán cho nền tảng Kobi E-learning.

## Giới thiệu

Đây là một microservice phụ trợ chịu trách nhiệm xử lý tất cả các logic liên quan đến thanh toán trong hệ thống. Nó được xây dựng bằng Spring Boot và được thiết kế để trở nên mạnh mẽ, có khả năng mở rộng và an toàn.

## Công nghệ sử dụng

*   **Java:** 21
*   **Framework:** Spring Boot 3.5.6
*   **Build Tool:** Maven
*   **Database:** MySQL
*   **Messaging:** Apache Kafka
*   **Security:** OAuth2 Resource Server

## Điều kiện tiên quyết

Trước khi bắt đầu, hãy đảm bảo bạn đã cài đặt các phần mềm sau:

*   JDK 21
*   Apache Maven
*   MySQL
*   Apache Kafka

## Cài đặt và Khởi chạy

1.  **Clone a repository:**
    ```bash
    git clone <your-repository-url>
    cd payment-service
    ```

2.  **Cấu hình ứng dụng:**
    Mở file `src/main/resources/application.properties` (hoặc `application.yml`) và cập nhật các thuộc tính sau cho phù hợp với môi trường cục bộ của bạn:
    *   Cấu hình kết nối cơ sở dữ liệu (URL, username, password)
    *   Cấu hình máy chủ Kafka
    *   Cấu hình máy chủ phát hành OAuth2

3.  **Build dự án:**
    ```bash
    mvn clean install
    ```

4.  **Chạy ứng dụng:**
    ```bash
    mvn spring-boot:run
    ```
    Ứng dụng sẽ khởi động và chạy trên cổng mặc định (thường là 8080).

## API Endpoints

*(Phần này mô tả các API endpoint chính do dịch vụ cung cấp. Bạn nên thêm tài liệu chi tiết cho từng endpoint ở đây.)*

---
*Tài liệu này được tạo tự động.*
