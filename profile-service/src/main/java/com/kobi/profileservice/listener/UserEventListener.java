package com.kobi.profileservice.listener;


import com.kobi.avro.UserCreatedEvent;
import com.kobi.profileservice.service.ProcessedEventService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEventListener {
    ProcessedEventService processedEventService;

    @KafkaListener(topics = "user.user-created.v1", groupId = "profile-service-group")
    public void handleUserCreated(@Payload UserCreatedEvent event) {

        processedEventService.processEvent(event);
    }
}

