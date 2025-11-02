package com.kobi.elearning.listener;

import com.kobi.elearning.service.UserService;
import event.ProfileCreationFailed;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompensationEventListener {
	UserService userService;
	@KafkaListener(topics = "user.profile-creation-failed.v1", groupId = "identity-service-compensation-group")
    public void handleProfileCreatedFailed(ProfileCreationFailed event) {
		log.warn("COMPENSATION: Received profile creation failure for user: {}. Initiating compensation logic.", event.getUserId());
		userService.compensateUserCreation(event.getUserId());
	}
}
