package com.kobi.paymentservice.entity;

import com.kobi.paymentservice.entity.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class OutboxEvent {
    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String aggregateType;
    String aggregateId;
    String eventType;
    @Column(columnDefinition = "TEXT")
    String payload;
    @Enumerated(EnumType.STRING)
    @Column(length = 20) // Chỉ định độ dài đủ lớn cho cột status
    EventStatus status;
    LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = EventStatus.NEW;
    }
}
