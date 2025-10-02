package com.kobi.reviewservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
	REVIEW_CREATED(200, "Review created successfully", HttpStatus.OK),
	GET_REVIEWS_SUCCESS(201, "Get reviews successfully", HttpStatus.OK);

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;
}
