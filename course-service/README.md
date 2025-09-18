# Course Service

Dịch vụ quản lý các khóa học và các phần của chúng trong nền tảng Kobi Elearning.

## Công nghệ sử dụng

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Web**: Để xây dựng các REST API.
- **Spring Data JPA**: Để tương tác với cơ sở dữ liệu.
- **MySQL**: Cơ sở dữ liệu quan hệ.
- **Spring Security (OAuth2 Resource Server)**: Để bảo mật các API endpoint.
- **Maven**: Công cụ quản lý dự án và build.
- **Lombok**: Giảm mã boilerplate.
- **MapStruct**: Ánh xạ giữa các đối tượng Java.

## API Endpoints

Dịch vụ cung cấp các API endpoint sau:

### Quản lý Khóa học

- `POST /v1/created`
  - **Mô tả**: Tạo một khóa học mới.
  - **Request Body**:
    ```json
    {
      "title": "string",
      "description": "string"
    }
    ```
  - **Response**: Trả về thông tin chi tiết của khóa học đã được tạo.

### Quản lý Phần học

- `POST /section/created/{courseId}`
  - **Mô tả**: Tạo một phần học mới cho một khóa học đã có.
  - **Path Variable**: `courseId` - ID của khóa học.
  - **Request Body**:
    ```json
    {
      "title": "string"
    }
    ```
  - **Response**: Trả về thông tin chi tiết của phần học đã được tạo.

## Cấu hình

Dự án không có tệp `application.properties` hoặc `application.yml`. Bạn cần tạo một tệp `application.yml` (hoặc `.properties`) trong thư mục `src/main/resources` và cung cấp các cấu hình cần thiết, đặc biệt là cấu hình kết nối cơ sở dữ liệu.

**Ví dụ `application.yml`:**

```yaml
server:
  port: 8081 # Hoặc cổng bạn muốn

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/kobi_course_db
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: <your_keycloak_issuer_uri>
          jwk-set-uri: <your_keycloak_jwk_set_uri>

```

## Làm thế nào để chạy

1.  **Cấu hình cơ sở dữ liệu**: Đảm bảo bạn đã tạo một cơ sở dữ liệu MySQL và cập nhật thông tin kết nối trong tệp `application.yml`.
2.  **Build dự án**:
    ```bash
    mvn clean install
    ```
3.  **Chạy ứng dụng**:
    ```bash
    mvn spring-boot:run
    ```

Dịch vụ sẽ khởi động và chạy trên cổng bạn đã cấu hình (mặc định là 8080 nếu không được chỉ định).
