package com.kobi.courseservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
	VALIDATION_ERROR(204, "Validation error", HttpStatus.BAD_REQUEST),
	AUTHENTICATION_FAILED(207, "Authentication failed", HttpStatus.UNAUTHORIZED),
	UNCATEGORIZED_EXCEPTION(209, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    COURSE_NOT_FOUND(210, "Course not found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(211, "User not found", HttpStatus.NOT_FOUND),
    SECTION_NOT_FOUND(212, "Section not found", HttpStatus.NOT_FOUND),
    LESSON_NOT_FOUND(213, "Lesson not found", HttpStatus.NOT_FOUND),

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
