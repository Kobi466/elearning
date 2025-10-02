package com.kobi.progressservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
	ENROLLMENT_SUCCESS(200, "Enrollment success", HttpStatus.OK),
	GET_MY_ENROLLMENT_SUCCESS(200, "Get my enrollment success", HttpStatus.OK),
	COMPLETE_LESSON_SUCCESS(200, "Complete lesson success", HttpStatus.OK),
	GET_COURSE_PROGRESS_SUCCESS(200, "Get course progress success", HttpStatus.OK);

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;
}
