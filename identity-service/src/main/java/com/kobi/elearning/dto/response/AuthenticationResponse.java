package com.kobi.elearning.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
	String accessToken;
	String refreshToken;
    Date accessTokenExpiresAt;
	UserResponse user;
}
