package com.kobi.paymentservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
	CREATE_PAYMENT_SUCCESS(200, "Create payment success", HttpStatus.OK);

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;
}
