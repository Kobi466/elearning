package com.kobi.profileservice.listener;


import com.kobi.avro.UserCreatedEvent;
import com.kobi.profileservice.service.ProcessedEventService;
import event.NotificationEvent;
import event.ProfileCreationFailed;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEventListener {
    ProcessedEventService processedEventService;
    KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "user.user-created.v1", groupId = "profile-service-group")
    public void handleUserCreated(@Payload UserCreatedEvent event) {
        try {
            processedEventService.processEvent(event);
            // send event success to notification service
            kafkaTemplate.send("onboard-success"
                    , NotificationEvent.builder()
                            .channel("Email")
                            .recipient(event.getUser().getEmail())
                            .template("Welcome to KOBI ELearning")
                            .param(Map.of("userName", event.getUser().getUserName()))
                            .build()
            );
        } catch (Exception e) {
            String userId = event.getUser().getUserId();
            var profileEventFail = ProfileCreationFailed.
                    builder()
                    .userId(userId)
                    .reason(e.getMessage())
                    .build();
            kafkaTemplate.send("user.profile-creation-failed.v1", userId, profileEventFail);
            log.error("Error occurred while processing user created event: {}", e.getMessage());
        }
    }
}

