package com.kobi.elearning.exception;

import org.hibernate.engine.jdbc.env.internal.LobCreationLogging_$logger;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode {
	USER_CREATED(1001, "User created successfully", HttpStatus.CREATED),
	USER_UPDATED(1002, "User updated successfully", HttpStatus.OK),
	LOGIN_SUCCESS(1003, "Login successful", HttpStatus.OK),
	INTROSPECT_SUCCESS(1004, "Introspection successful", HttpStatus.OK),
	USER_FETCH_SUCCESS(1005, "User fetched successfully", HttpStatus.OK),
	REFRESH_TOKEN_SUCCESS(1006, "Refresh token successful", HttpStatus.OK),
	LOGOUT_SUCCESS(1007, "Logout successful", HttpStatus.OK),
	CREATION_PASSWORD_SUCCESS(1008, "Creation password successful", HttpStatus.OK)
	;

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;
}
