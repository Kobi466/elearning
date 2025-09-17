package com.kobi.profileservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
        PROFILE_CREATED(201,"Profile created successfully", HttpStatus.CREATED),
    PROFILE_UPDATED(200, "Profile updated successfully", HttpStatus.OK),
    AVATAR_UPDATED(200, "Avatar updated successfully", HttpStatus.OK),
    GET_PROFILE_SUCCESS(200, "Get profile successfully", HttpStatus.OK),
    DELETE_ALL_SUCCESS(200, "Delete all success", HttpStatus.OK),
	;

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;
}
