package com.kobi.profileservice.controller;

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
public class ProfileConsumer {
    ProfileService profileService;
    @KafkaListener(topics = "user.created")
    public void listenUserCreateEvent(UserCreateEvent message) {
        log.info("Received message: {}", message);
        profileService.createProfile(message);
    }
}
