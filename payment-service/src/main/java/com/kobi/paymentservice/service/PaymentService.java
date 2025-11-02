package com.kobi.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobi.event.EnrollmentFailedEvent;
import com.kobi.event.EnrollmentSuccessEvent;
import com.kobi.paymentservice.entity.enums.OrderStatus;
import com.kobi.paymentservice.exception.AppException;
import com.kobi.paymentservice.exception.ErrorCode;
import com.kobi.paymentservice.repository.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentService {
    PaymentRepository paymentRepository;
    ObjectMapper objectMapper;

    @Transactional
    public void updateStatusOrderFromEnrollment(String event)  {
        try {
            JsonNode rootNode = objectMapper.readTree(event);
            String eventType = rootNode.get("eventType").asText();
            String orderId = rootNode.get("orderId").asText();
            var order = paymentRepository.findById((orderId))
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
            if (!order.getStatus().equals(OrderStatus.PAID)) {
                return;
            }
            switch (eventType) {
                case "ENROLLMENT_SUCCESS" -> {
                    order.setStatus(OrderStatus.FULFILLED);
                    log.info("Payment successful");
                    return;
                }
                case "ENROLLMENT_FAILED" -> {
                    order.setStatus(OrderStatus.FAILED);
                    log.info("Payment failed");
                    return;
                }
                default -> log.warn("Unknown event type: {}", eventType);
            }
            paymentRepository.save(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
