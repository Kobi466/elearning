package com.kobi.enrollmentservice.listener;

import com.kobi.enrollmentservice.service.ProcessEventService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentEnrollment {
    ProcessEventService processEventService;
    @KafkaListener(topics = "payment_events", groupId = "enrollment-service-group")
    public void listenerEvent(String event){
        processEventService.processEvent(event);
    }
}
