package com.kobi.elearning.service.implement;


import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kobi.elearning.constant.PredefinedRole;
import com.kobi.elearning.dto.request.auth.*;
import com.kobi.elearning.dto.response.auth.AuthenticationResponse;
import com.kobi.elearning.dto.response.auth.IntrospectResponse;
import com.kobi.elearning.dto.response.auth.RefreshTokenResponse;
import com.kobi.elearning.entity.RefreshToken;
import com.kobi.elearning.entity.Role;
import com.kobi.elearning.entity.User;
import com.kobi.elearning.exception.AppException;
import com.kobi.elearning.exception.ErrorCode;
import com.kobi.elearning.mapper.UserMapper;
import com.kobi.elearning.repository.RefreshTokenRepository;
import com.kobi.elearning.repository.RoleRepository;
import com.kobi.elearning.repository.UserRepository;
import com.kobi.elearning.repository.httpclient.GoogleOauth2Client;
import com.kobi.elearning.repository.httpclient.GoogleUserInfoClient;
import com.kobi.elearning.service.AuthenticationService;
import com.kobi.elearning.service.JwtService;
import com.kobi.elearning.service.RedisService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class AuthenticationServiceImpl implements AuthenticationService {
	UserRepository userRepository;
	RoleRepository roleRepository;
	UserMapper userMapper;
	PasswordEncoder passwordEncoder;
	JwtService jwtService;
	RefreshTokenRepository refreshTokenRepository;
	RedisService redisService;
	GoogleOauth2Client googleOauth2Client;
	GoogleUserInfoClient googleUserInfoClient;
	@NonFinal
	@Value( "${google.client-id}")
	String clientId;
	@NonFinal
	@Value( "${google.client-secret}")
	String clientSecret;
	@NonFinal
	@Value( "${google.grant-type}")
	String grantType;
	@NonFinal
	@Value( "${google.redirect-uri}")
	String redirectUrl;

	@Override
	public AuthenticationResponse authenticateUserGoogle(String code) {
		var response = googleOauth2Client.exchangeToken(ExchangeTokenRequest.builder()
				.clientId(clientId)
				.clientSecret(clientSecret)
				.code(code)
				.grantType(grantType)
				.redirectUri(redirectUrl)
				.build()
		);
		log.info("TOKEN RESPONSE {}", response);

		var userGg = googleUserInfoClient.getUserInfo("json", response.getAccessToken());
		log.info("USER GG {}", userGg);

		Set<Role> role = new HashSet<>();
		role.add(roleRepository.findByName(PredefinedRole.STUDENT));
		var user = userRepository.findByUserName(userGg.getEmail())
				.orElseGet(
				()-> userRepository.save(User
						.builder()
						.userName(userGg.getEmail())
						.roles(role)
						.fullName(userGg.getName())
						.oauth2Account(true)
						.build()
				)
		);
		String accessToken = jwtService.generateAccessToken(user);

		return AuthenticationResponse
				.builder()
				.accessToken(accessToken)
				.refreshToken(generateAndSaveRefreshToken(user))
				.expiresAt(jwtService.getExpirationDateFromToken(accessToken))
				.user(userMapper.toUserResponse(user))
				.build();
	}

	@Override
	public AuthenticationResponse authenticateUser(LoginRequest request) {
		User user = userRepository.findByUserName(request.getUserName())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		boolean isAuthenticated = passwordEncoder.matches(request.getPassWord(), user.getPassWord());
		if (!isAuthenticated) {
			log.error("Authentication failed for user {}", request.getUserName());
			throw new AppException(ErrorCode.AUTHENTICATION_FAILED);
		}
		String accessToken = jwtService.generateAccessToken(user);
		return AuthenticationResponse.builder()
				.accessToken(accessToken)
				.refreshToken(generateAndSaveRefreshToken(user))
				.expiresAt(jwtService.getExpirationDateFromToken(accessToken))
				.user(userMapper.toUserResponse(user))
				.build();
	}
	private String generateAndSaveRefreshToken(User user) {
		String refreshToken = jwtService.generateRefreshToken(user);
		String idUser = jwtService.getIdFromToken(refreshToken);
		User oneuser = userRepository.findById(idUser)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		refreshTokenRepository.save(RefreshToken.builder()
				.user(oneuser)
				.token(refreshToken)
				.expiryTime(jwtService.getExpirationDateFromToken(refreshToken))
				.used(false)
				.revoked(false)
				.createdAt(jwtService.getIssuedAtDateFromToken(refreshToken))
				.build()
		);
		return refreshToken;
	}

	@Override
	public IntrospectResponse introspectToken(IntrospectRequest request) {
		boolean blackListed =redisService.isBlackListed(request.getToken());
		if (blackListed) {
			log.error("Token has been blacklisted: {}", request.getToken());
			throw new AppException(ErrorCode.FAILED_TOKEN);
		}
		return IntrospectResponse.builder()
				.valid(jwtService.validateToken(request.getToken()))
				.build();
	}
	@Override
	public RefreshTokenResponse refreshToken (RefreshTokenRequest request) {
		boolean isValid = jwtService.validateToken(request.getToken());
		if (!isValid) {
			log.error("Invalid refresh token: {}", request.getToken());
			throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
		}
		RefreshToken reFreshToken = refreshTokenRepository.findByToken(request.getToken());
		if (reFreshToken.isUsed() || reFreshToken.isRevoked() ) {
			log.error("Refresh token has already been used or revoked: {}", request.getToken());
			throw new AppException(ErrorCode.REFRESH_TOKEN_ALREADY_USED_OR_REVOKED);
		}
		Date issuedAt = jwtService.getExpirationDateFromToken(request.getToken());
		if (reFreshToken.getExpiryTime().before(new java.util.Date())) {
			log.error("Refresh token has expired: {}", request.getToken());
			throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
		}
		reFreshToken.setUsed(true);
		reFreshToken.setRevoked(true);
		refreshTokenRepository.save(reFreshToken);
		User user = userRepository.findById(jwtService
						.getIdFromToken(request.getToken()))
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		String newAccessToken = jwtService.generateAccessToken(user);
		String newRefreshToken = jwtService.generateRefreshToken(user);
		refreshTokenRepository.save(RefreshToken
				.builder()
				.user(reFreshToken.getUser())
				.token(newRefreshToken)
				.expiryTime(jwtService.getExpirationDateFromToken(newRefreshToken))
				.used(false)
				.revoked(false)
				.createdAt(jwtService.getIssuedAtDateFromToken(newRefreshToken))
				.build());
		return RefreshTokenResponse.builder()
				.accessTokenNew(newAccessToken)
				.refreshTokenNew(newRefreshToken)
				.accessTokenExpiration(jwtService
						.getExpirationDateFromToken(newAccessToken))
				.build();
	}

	@Override
	public void revokedToken (LogoutRequest request){
		boolean isValid = jwtService.validateToken(request.getToken());
		if (!isValid) {
			log.error("Invalid access token: {}", request.getToken());
			throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
		}
		redisService.blackListAccessToken(request.getToken(), jwtService.getExpirationTimeFromToken(request.getToken()));
		String userId = jwtService.getIdFromToken(request.getToken());
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		List<RefreshToken> tokenList = refreshTokenRepository.findAllByUser(user);
		tokenList.forEach(token -> {
			token.setRevoked(true);
			token.setUsed(true);
		});
		refreshTokenRepository.saveAll(tokenList);
	}
}
