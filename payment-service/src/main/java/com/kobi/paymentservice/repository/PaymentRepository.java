package com.kobi.paymentservice.repository;

import com.kobi.paymentservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Order, String> {
    Optional<Order> findByOrderRef(String orderRef);
}
