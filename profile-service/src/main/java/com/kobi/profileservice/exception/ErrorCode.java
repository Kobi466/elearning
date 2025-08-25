package com.kobi.profileservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    AUTHENTICATION_FAILED(401, "Authentication failed", HttpStatus.UNAUTHORIZED),
    VALIDATION_ERROR(402, "Validation error", HttpStatus.BAD_REQUEST),
    USER_EXISTS(403, "User already exists", HttpStatus.CONFLICT),
    ;

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;

	ErrorCode(int status, String message, HttpStatus httpStatus) {
		this.status = status;
		this.message = message;
		this.httpStatus = httpStatus;
	}
}
