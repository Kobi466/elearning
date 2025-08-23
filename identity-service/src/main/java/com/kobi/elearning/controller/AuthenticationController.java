package com.kobi.elearning.controller;

import com.kobi.elearning.dto.request.IntrospectRequest;
import com.kobi.elearning.dto.request.LoginRequest;
import com.kobi.elearning.dto.request.LogoutRequest;
import com.kobi.elearning.dto.request.RefreshTokenRequest;
import com.kobi.elearning.dto.response.ApiResponse;
import com.kobi.elearning.dto.response.AuthenticationResponse;
import com.kobi.elearning.dto.response.IntrospectResponse;
import com.kobi.elearning.dto.response.RefreshTokenResponse;
import com.kobi.elearning.exception.SuccessCode;
import com.kobi.elearning.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	AuthenticationService authenticationService;

	@PostMapping("/login")
	ApiResponse<AuthenticationResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
		return ApiResponse.ok(authenticationService.authenticateUser(request), SuccessCode.LOGIN_SUCCESS);
	}

	@PostMapping("/introspect")
	ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
		return ApiResponse.ok(authenticationService.introspectToken(request), SuccessCode.INTROSPECT_SUCCESS);
	}

	@PostMapping("/refresh")
	ApiResponse<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
		return ApiResponse.ok(authenticationService.refreshToken(request), SuccessCode.REFRESH_TOKEN_SUCCESS);
	}

	@PostMapping("/logout")
	ApiResponse<Void> logout( @Valid @RequestBody LogoutRequest request) {
		authenticationService.revokedToken(request);
		return ApiResponse.ok(null, SuccessCode.LOGOUT_SUCCESS);
	}
}
