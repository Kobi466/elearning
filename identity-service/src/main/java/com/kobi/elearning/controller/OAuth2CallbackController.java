package com.kobi.elearning.controller;

import com.kobi.elearning.dto.response.ApiResponse;
import com.kobi.elearning.dto.response.AuthenticationResponse;
import com.kobi.elearning.exception.SuccessCode;
import com.kobi.elearning.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
