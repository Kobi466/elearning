package com.kobi.elearning.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.Instant;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(
        name = "outbox_event",
        indexes = {
                @Index(name = "idx_outbox_status", columnList = "status"),
                @Index(name = "idx_outbox_aggregate", columnList = "aggregateType, aggregateId"),
                @Index(name = "idx_outbox_corr", columnList = "correlationId")
        }
)
public class OutboxEvent {

    @Id
    String id;
    // 🔹 ID duy nhất của sự kiện (UUID).
    // VD: "2a0f6eaa-0dcd-4d4a-9f67-3b21aef3db3b"

    @Column(nullable = false)
    String aggregateType;
    // 🔹 Loại Aggregate (thực thể nguồn) sinh ra sự kiện.
    // VD: "USER", "ORDER"
    // Khi user được tạo: aggregateType = "USER"

    @Column(nullable = false)
    String aggregateId;
    // 🔹 ID của aggregate (thực thể).
    // VD: userId = "12345"
    // aggregateId = "12345"

    @Column(nullable = false)
    String eventType;
    // 🔹 Loại sự kiện.
    // VD: "USER_CREATED", "USER_UPDATED"
    // Khi user mới được tạo: eventType = "USER_CREATED"

    @Column(nullable = false)
    @Builder.Default

    Integer version = 1;
    // 🔹 Version của schema sự kiện (dùng khi thay đổi cấu trúc message).
    // VD: version = 1 (sau này nếu thêm field thì version = 2)

    @Column(nullable = false, length = 1000)
    byte[] payload;
    // 🔹 Nội dung data (message) của sự kiện, thường là JSON hoặc Avro bytes.
    // VD (JSON):
    // {
    //   "id": "12345",
    //   "email": "abc@gmail.com",
    //   "name": "Kobi"
    // }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default

    Status status = Status.NEW;
    // 🔹 Trạng thái sự kiện:
    // - NEW: mới tạo, chưa publish
    // - PUBLISHED: đã publish thành công
    // - FAILED: publish thất bại
    // VD: khi insert: status = NEW

    @Column(nullable = false)
    @Builder.Default
    Instant createdAt = Instant.now();
    // 🔹 Thời điểm tạo event trong DB.
    // VD: "2025-08-27T09:15:30Z"

    Instant publishedAt;
    // 🔹 Thời điểm publish thành công lên Kafka.
    // VD: "2025-08-27T09:15:35Z"

    @Column(nullable = false)
    @Builder.Default

    Integer retryCount = 0;
    // 🔹 Số lần retry publish sự kiện (nếu lỗi).
    // VD: retryCount = 3 (đã thử publish 3 lần)

    @Column(length=1000)
    String lastError;
    // 🔹 Lưu error message khi publish lỗi.
    // VD: "Kafka timeout after 5s"

    @Column(length=100)
    String correlationId;
    // 🔹 ID để correlate log/trace giữa các service.
    // VD: correlationId = requestId của API
    // Khi user-service gọi sang profile-service → đều dùng chung correlationId.

    @Column(length=100)
    String traceContext;
    // 🔹 Dùng cho distributed tracing (OpenTelemetry/Jaeger/Zipkin).
    // VD: traceContext = "traceId=abc123-spanId=xyz456"

    @Column(length=100)
    String source;
    // 🔹 Service phát ra sự kiện.
    // VD: "identity-service"

    @Column(length=128)
    String partitionKey;
    // 🔹 Dùng để quyết định Kafka partition.
    // VD: partitionKey = userId = "12345" (đảm bảo tất cả event của user này đi vào 1 partition)

    public enum Status {
        NEW,
        PUBLISHED,
        FAILED
    }
}
