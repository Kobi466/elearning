package com.kobi.enrollmentservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
	ENROLLMENT_SUCCESS(200, "Enrollment success", HttpStatus.OK),
	GET_MY_ENROLLMENT_SUCCESS(200, "Get my enrollment success", HttpStatus.OK);

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;
}
