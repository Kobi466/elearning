package com.kobi.reviewservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
	VALIDATION_ERROR(204, "Validation error", HttpStatus.BAD_REQUEST),
	AUTHENTICATION_FAILED(207, "Authentication failed", HttpStatus.UNAUTHORIZED),
	REVIEW_EXISTS(208, "Review exists", HttpStatus.BAD_REQUEST);

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;

	ErrorCode(int status, String message, HttpStatus httpStatus) {
		this.status = status;
		this.message = message;
		this.httpStatus = httpStatus;
	}
}
