package com.kobi.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.ALWAYS)//non_null thi khong hien nhung null
public class ApiResponse<T> {
	int status = 200; // Default status code
	String message;
	T data;
}
