package com.kobi.elearning.service.implement;


import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.kobi.elearning.entity.User;
import com.kobi.elearning.exception.AppException;
import com.kobi.elearning.exception.ErrorCode;
import com.kobi.elearning.service.JwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtServiceImpl implements JwtService {

	@Value("${jwt.secret}")
	String secretKey;
	@Value("${jwt.access-token-expiration}")
	long accessTokenExpiration;
	@Value("${jwt.refresh-token-expiration}")
	long refreshTokenExpiration;

	@Override
	public String generateAccessToken(User user) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject(user.getId())
				.claim("userName", user.getUserName())
				.claim("scope", buildScope(user))
				.issuer("com.kobi.elearning")
				.issueTime(new java.util.Date())
				.jwtID(UUID.randomUUID().toString())
				.expirationTime(new java.util.Date(System.currentTimeMillis() + accessTokenExpiration))
				.build();
		JWSObject jwsObject = new JWSObject(header, new Payload(claimsSet.toJSONObject()));
		try {
			jwsObject.sign(new MACSigner(secretKey));
		} catch (JOSEException e) {
			throw new AppException(ErrorCode.FAILED_TOKEN);
		}
		return jwsObject.serialize();
	}

	@Override
	public String generateRefreshToken(User user) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject(user.getId())
				.claim("type", "refresh")
				.issueTime(new java.util.Date())
				.expirationTime(new java.util.Date(System.currentTimeMillis() + refreshTokenExpiration))
				.jwtID(UUID.randomUUID().toString())
				.build();
		JWSObject jwsObject = new JWSObject(header, new Payload(claimsSet.toJSONObject()));
		try {
			jwsObject.sign(new MACSigner(secretKey));
		} catch (JOSEException e) {
			throw new AppException(ErrorCode.FAILED_TOKEN);
		}
		return jwsObject.serialize();
	}

	@Override
	public boolean validateToken(String token) {
		if (token == null || token.isBlank()) {
			log.warn("Token is null or blank");
			return false;
		}
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			JWSVerifier verifier = new MACVerifier(secretKey);
			// 1. Kiểm tra chữ ký hợp lệ
			if (!signedJWT.verify(verifier)) {
				return false;
			}
			// 2. Kiểm tra thời gian hết hạn
			Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
			if (expirationTime.before(new Date())) {
				log.warn("Token expired at {}", expirationTime);
				return false;
			}
			// 3. Kiểm tra token có đúng user không (optional, tăng bảo mật)
//            String subject = signedJWT.getJWTClaimsSet().getSubject();
//            if (!subject.equals(user.getId())) {
//                log.warn("Token subject {} does not match user ID {}", subject, user.getId());
//                return false;
//            }
			return true;
		} catch (ParseException | JOSEException e) {
			log.error("Failed to validate token: {}", e.getMessage());
			throw new AppException(ErrorCode.FAILED_TOKEN);
		}
	}

	private String buildScope(User user) {
		StringJoiner stringJoiner = new StringJoiner(" ");

		if (!CollectionUtils.isEmpty(user.getRoles()))
			user.getRoles().forEach(role -> {
				stringJoiner.add("ROLE_" + role.getName());
				if (!CollectionUtils.isEmpty(role.getPermissions()))
					role.getPermissions()
							.forEach(permission -> stringJoiner.add(permission.getName()));
			});

		return stringJoiner.toString();
	}
	@Override
	public String getIdFromToken(String token) {
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			return signedJWT.getJWTClaimsSet().getSubject();
		} catch (ParseException e) {
			log.error("Failed to parse token: {}", e.getMessage());
			throw new AppException(ErrorCode.FAILED_TOKEN);
		}
	}
	@Override
	public Date getExpirationDateFromToken(String token) {
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			return signedJWT.getJWTClaimsSet().getExpirationTime();
		} catch (ParseException e) {
			log.error("Failed to parse token: {}", e.getMessage());
			throw new AppException(ErrorCode.FAILED_TOKEN);
		}
	}

	@Override
	public Date getIssuedAtDateFromToken(String token) {
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			return signedJWT.getJWTClaimsSet().getIssueTime();
		} catch (ParseException e) {
			log.error("Failed to parse token: {}", e.getMessage());
			throw new AppException(ErrorCode.FAILED_TOKEN);
		}
	}

	@Override
	public long getExpirationTimeFromToken(String token) {
		return getExpirationDateFromToken(token).getTime() - System.currentTimeMillis();
	}
}
