package com.kobi.elearning.dto.request.auth;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PasswordCreationRequest {
	@Size(min = 8, message = "Password must be at least {min} characters")
	String passWord;
}
