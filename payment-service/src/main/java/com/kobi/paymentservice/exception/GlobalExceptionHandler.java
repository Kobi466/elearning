package com.kobi.paymentservice.exception;


import com.kobi.paymentservice.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AppException.class)
	ResponseEntity<ApiResponse<Object>> handleAppException(AppException e) {
	ErrorCode errorCode = e.getErrorCode();
		return ResponseEntity
				.status(e.getErrorCode().getHttpStatus())
				.body(ApiResponse.error(null, errorCode));
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<String> errorMessages = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(FieldError::getDefaultMessage)
				.collect(Collectors.toList());
		ApiResponse<Object> response = ApiResponse.error(errorMessages, ErrorCode.VALIDATION_ERROR);
		return ResponseEntity.badRequest().body(response);
	}
}
