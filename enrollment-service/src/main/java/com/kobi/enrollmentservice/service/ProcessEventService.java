package com.kobi.enrollmentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobi.enrollmentservice.entity.ProcessEvent;
import com.kobi.enrollmentservice.repository.ProcessEventRepository;
import com.kobi.event.EnrollmentFailedEvent;
import com.kobi.event.EnrollmentSuccessEvent;
import com.kobi.event.OrderPaidEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProcessEventService {
    @NonFinal
    public static final String ENROLLMENT_EVENTS_TOPIC = "enrollment_events";
    ProcessEventRepository processEventRepository;
    ObjectMapper objectMapper;
    EnrollmentService enrollmentService;
    KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    public void processEvent(String event) {
        OrderPaidEvent objPayload = null;
        try {
            objPayload = objectMapper.readValue(event, OrderPaidEvent.class);
            String eventId = objPayload.getEventId();
            //IDEMPOTENCY
            if (processEventRepository.existsByEventId(eventId)) {
                log.warn("Sự kiện {} đã được xử lý trước đó. Bỏ qua.", eventId);
                return;
            }
//            //Test event failed
//            if(true){
//                throw new RuntimeException("Test Failed");
//            }
            enrollmentService.internalEnrollmentNoFree(objPayload.getUserId(), objPayload.getCourseId());
            processEventRepository.save(ProcessEvent.builder()
                    .eventId(objPayload.getEventId())
                    .processAt(objPayload.getCreatedAt())
                    .build());
            //Success
            String enrollSuccess = objectMapper.writeValueAsString(EnrollmentSuccessEvent
                    .builder()
                    .userId(objPayload.getUserId())
                    .orderId(objPayload.getOrderId())
                    .courseId(objPayload.getCourseId())
                    .eventType("ENROLLMENT_SUCCESS")
                    .build());
            kafkaTemplate.send(ENROLLMENT_EVENTS_TOPIC, enrollSuccess);
            log.info("Đã gửi thành công event phản hồi ghi danh thành công{} lên Kafka topic {}.", enrollSuccess, ENROLLMENT_EVENTS_TOPIC);
        } catch (Exception e) {
            //Failed
            log.error("Enrollment failed {}", e);
            try {
                if (objPayload != null) {
                    String enrollmentFailed = objectMapper.writeValueAsString(EnrollmentFailedEvent
                            .builder()
                            .orderId(objPayload.getOrderId())
                            .userId(objPayload.getUserId())
                            .courseId(objPayload.getCourseId())
                            .eventType("ENROLLMENT_FAILED")
                            .build());
                    kafkaTemplate.send(ENROLLMENT_EVENTS_TOPIC, enrollmentFailed);
                }
            } catch (JsonProcessingException ex) {
                log.error("Không thể serialize payload báo cáo thất bại: {}", ex.getMessage());
            }
            //Error rollback
//            throw new RuntimeException(e);
        }
    }
}
