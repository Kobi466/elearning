package com.kobi.profileservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    AUTHENTICATION_FAILED(401, "Authentication failed", HttpStatus.UNAUTHORIZED),
    USER_EXISTS(403, "User already exists", HttpStatus.CONFLICT),
    VALIDATION_ERROR(400, "Validation error", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND);

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;

	ErrorCode(int status, String message, HttpStatus httpStatus) {
		this.status = status;
		this.message = message;
		this.httpStatus = httpStatus;
	}
}
