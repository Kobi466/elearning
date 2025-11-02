package com.kobi.paymentservice.entity;

import com.kobi.paymentservice.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false)
    String userId;
    @Column(nullable = false)
    String courseId;
    @Column(nullable = false)
    BigDecimal amount;
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    OrderStatus status;
    @Column(nullable = false, unique = true)
    String orderRef;//vnp_TxnRef
    String gatewayTransId;//vnp_TransactionNo
    String payDate; //vnp_PayDate
    @Column(nullable = false)
    String paymentGateway;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    void prePersist()
    {
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
        this.updatedAt = LocalDateTime.now();
    }





}
