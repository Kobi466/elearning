package com.kobi.paymentservice.repository;

import com.kobi.paymentservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Order, String> {

}
