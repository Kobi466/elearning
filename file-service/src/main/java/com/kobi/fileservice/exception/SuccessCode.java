package com.kobi.fileservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
	UPLOAD_SUCCESS(200, "Upload success", HttpStatus.OK),
	;

	private final int status;
	private final String message;
	private final HttpStatus httpStatus;
}
