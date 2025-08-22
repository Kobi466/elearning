package com.kobi.elearning.dto.request.profile;

import java.util.List;

import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
	@Size(min = 4, message = "Username must be at least {min} characters")
	String userName;
	@Size(min = 2, message = "Full name must be at least {min} characters")
	String fullName;
	List<String> roles;
}
