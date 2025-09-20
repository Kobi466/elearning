package com.kobi.courseservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kobi.courseservice.exception.ErrorCode;
import com.kobi.courseservice.exception.SuccessCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)//non_null thi khong hien nhung null
public class ApiResponse<T> {
	int status = 200; // Default status code
	String message;
	T data;

	public static <T> ApiResponse<T> ok(T data, SuccessCode successCode) {
		return ApiResponse.<T>builder()
				.status(successCode.getStatus())
				.message(successCode.getMessage())
				.data(data)
				.build();
	}
	public static <T> ApiResponse<T> error(ErrorCode errorCode) {
		return ApiResponse.<T>builder()
				.status(errorCode.getStatus())
				.message(errorCode.getMessage())
				.build();
	}
	public static <T> ApiResponse<T> error(T data, ErrorCode errorCode) {
		return ApiResponse.<T>builder()
				.status(errorCode.getStatus())
				.message(errorCode.getMessage())
				.data(data)
				.build();
	}
}
