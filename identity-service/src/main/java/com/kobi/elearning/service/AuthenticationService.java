package com.kobi.elearning.service;


import com.kobi.elearning.dto.request.IntrospectRequest;
import com.kobi.elearning.dto.request.LoginRequest;
import com.kobi.elearning.dto.request.LogoutRequest;
import com.kobi.elearning.dto.request.RefreshTokenRequest;
import com.kobi.elearning.dto.response.AuthenticationResponse;
import com.kobi.elearning.dto.response.IntrospectResponse;
import com.kobi.elearning.dto.response.RefreshTokenResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

	AuthenticationResponse authenticateUserGoogle(String code);

	AuthenticationResponse authenticateUser(LoginRequest loginRequest);

	RefreshTokenResponse refreshToken(RefreshTokenRequest request);

	IntrospectResponse introspectToken(IntrospectRequest request);

	void revokedToken(LogoutRequest request);
}
