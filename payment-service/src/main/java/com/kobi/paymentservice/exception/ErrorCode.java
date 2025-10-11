package com.kobi.paymentservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
	VALIDATION_ERROR(204, "Validation error", HttpStatus.BAD_REQUEST),
	AUTHENTICATION_FAILED(207, "Authentication failed", HttpStatus.UNAUTHORIZED),
	COURSE_NO_FREE(208, "This course requires payment", HttpStatus.BAD_REQUEST),
	REGISTERED(209,"You have already enrolled in this course", HttpStatus.BAD_REQUEST),
	COURSE_NOT_FOUND(210, "Course not found", HttpStatus.NOT_FOUND),
    KAFKA_ERROR(211, "Kafka error", HttpStatus.INTERNAL_SERVER_ERROR);

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;

	ErrorCode(int status, String message, HttpStatus httpStatus) {
		this.status = status;
		this.message = message;
		this.httpStatus = httpStatus;
	}
}
