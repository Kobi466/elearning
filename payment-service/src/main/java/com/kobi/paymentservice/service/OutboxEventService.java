package com.kobi.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobi.event.OrderPaidEvent;
import com.kobi.event.OutboxEventCreatedEvent;
import com.kobi.paymentservice.entity.Order;
import com.kobi.paymentservice.entity.OutboxEvent;
import com.kobi.paymentservice.repository.OutboxEventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OutboxEventService {
    OutboxEventRepository outboxEventRepository;
    ObjectMapper objectMapper;
    ApplicationEventPublisher eventPublisher;

    public void createAndPublishOutboxEvent(Order order, String eventType) {
        try {
            var outboxEvent = OutboxEvent.builder()
                    .id(UUID.randomUUID().toString())
                    .aggregateId(order.getId())
                    .aggregateType("Order")
                    .eventType(eventType)
                    .build();
            var orderPaidEvent = OrderPaidEvent.builder()
                    .eventId(outboxEvent.getId())//----
                    .userId(order.getUserId())
                    .courseId(order.getCourseId())
                    .amount(order.getAmount())
                    .orderId(order.getId())
                    .createdAt(order.getCreatedAt())
                    .build();
            var payLoad = objectMapper.writeValueAsString(orderPaidEvent);
            outboxEvent.setPayload(payLoad);
            outboxEventRepository.save(outboxEvent);
            // Phát sự kiện nội bộ để trigger publisher
            var eventCreate = new OutboxEventCreatedEvent(outboxEvent);
            // Sử dụng ApplicationEventPublisher để phát sự kiện một cách chính xác
            eventPublisher.publishEvent(eventCreate);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
