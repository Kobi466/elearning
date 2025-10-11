package com.kobi.enrollmentservice.listener;

import com.kobi.enrollmentservice.dto.request.InternalEnrollmentRequest;
import com.kobi.enrollmentservice.service.EnrollmentService;
import event.EnrollmentPayment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentEnrollment {
    EnrollmentService enrollmentService;

    @KafkaListener(topics = "enrollment-user", groupId = "enrollment-service-group")
    public void listenerEvent(EnrollmentPayment event){
        enrollmentService.internalEnrollmentNoFree(event);
    }
}
