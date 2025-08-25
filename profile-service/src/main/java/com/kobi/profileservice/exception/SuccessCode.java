package com.kobi.profileservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
        PROFILE_CREATED(201,"Profile created successfully", HttpStatus.CREATED),
	;

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;
}
