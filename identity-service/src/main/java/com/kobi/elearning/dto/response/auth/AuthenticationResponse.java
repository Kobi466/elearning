package com.kobi.elearning.dto.response.auth;

import java.util.Date;

import com.kobi.elearning.dto.response.profile.UserResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
	String accessToken;
	String refreshToken;
	Date expiresAt;
	UserResponse user;
}
