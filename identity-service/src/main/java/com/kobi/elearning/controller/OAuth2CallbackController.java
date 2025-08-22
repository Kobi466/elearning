package com.kobi.elearning.controller;

import org.springframework.web.bind.annotation.*;

import com.kobi.elearning.dto.response.ApiResponse;
import com.kobi.elearning.dto.response.auth.AuthenticationResponse;
import com.kobi.elearning.exception.SuccessCode;
import com.kobi.elearning.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2")
@Slf4j
@FieldDefaults (level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2CallbackController {
	AuthenticationService authenticationService;
	@PostMapping("/callback")
	ApiResponse<AuthenticationResponse> callback(@RequestParam("code") String code) {
		return ApiResponse.ok(authenticationService.authenticateUserGoogle(code), SuccessCode.LOGIN_SUCCESS);
	}
}
