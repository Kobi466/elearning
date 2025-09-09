package com.kobi.elearning.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.kobi.avro.ProfileCreationFailedEvent;
import com.kobi.elearning.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompensationEventListener {
	UserService userService;
	@KafkaListener(topics = "user.profile-creation-failed.v1", groupId = "identity-service-compensation-group")
	public void handleProfileCreatedFailed(@Payload ProfileCreationFailedEvent event){
		log.warn("COMPENSATION: Received profile creation failure for user: {}. Initiating compensation logic.", event.getUserId());
		userService.compensateUserCreation(event.getUserId());
	}
}
