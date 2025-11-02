package com.kobi.paymentservice.repository;

import com.kobi.paymentservice.entity.OutboxEvent;
import com.kobi.paymentservice.entity.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, String> {
    List<OutboxEvent> findOutboxEventByStatus(EventStatus status);
}
