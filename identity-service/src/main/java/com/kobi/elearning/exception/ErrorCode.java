package com.kobi.elearning.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	USER_ALREADY_EXISTS(201,"User already exists", HttpStatus.BAD_REQUEST),
	USERNAME_INVALID(202,"Username is invalid", HttpStatus.BAD_REQUEST),
	PASSWORD_INVALID(203,"Password is invalid", HttpStatus.BAD_REQUEST),
	VALIDATION_ERROR(204, "Validation error", HttpStatus.BAD_REQUEST),
	USER_NOT_FOUND(205, "User not found", HttpStatus.NOT_FOUND),
	FAILED_TOKEN(206, "Failed to generate token", HttpStatus.INTERNAL_SERVER_ERROR),
	AUTHENTICATION_FAILED(207, "Authentication failed", HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED(208, "Unauthorized access", HttpStatus.UNAUTHORIZED),
	UNCATEGORIZED_EXCEPTION(209, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
	REFRESH_TOKEN_ALREADY_USED_OR_REVOKED (210, "Refresh token already used or revoked", HttpStatus.UNAUTHORIZED),
	INVALID_REFRESH_TOKEN (210, "Invalid refresh token", HttpStatus.UNAUTHORIZED),
	PASSWORD_EXISTED(211, "Password already existed", HttpStatus.BAD_REQUEST)
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
