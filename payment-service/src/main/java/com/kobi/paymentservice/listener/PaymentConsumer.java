package com.kobi.paymentservice.listener;

import com.kobi.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentConsumer {
    PaymentService paymentService;

    @KafkaListener(topics = "enrollment_events", groupId = "payment-service-group")
    public void listenerEvent(String event) {
        log.info("Received event: {}", event);
        paymentService.updateStatusOrderFromEnrollment(event);
    }

}
