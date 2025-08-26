package com.kobi.profileservice.listener;

import com.kobi.event.UserCreateEvent;
import com.kobi.profileservice.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEventListener {
    ProfileService profileService;

    @KafkaListener(topics = "${kafka.topic.user.created}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleUserCreated(UserCreateEvent event) {
        log.info("Received user created event for userId: {}", event.getUserId());
        profileService.createProfile(event);
    }
}
