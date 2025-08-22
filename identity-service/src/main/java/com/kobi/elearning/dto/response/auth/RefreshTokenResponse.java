package com.kobi.elearning.dto.response.auth;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RefreshTokenResponse {
	String accessTokenNew;
	String refreshTokenNew;
	Date accessTokenExpiration;
}
