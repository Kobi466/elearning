package com.kobi.paymentservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
	;

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;
}
