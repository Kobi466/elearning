package com.kobi.elearning.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.kobi.elearning.service.RedisService;

@Component
public class CustomJwtDecoder implements JwtDecoder {
	@Value("${jwt.secret}")
	private String secretKey;

//	@Autowired
//	private AuthenticationService authenticationService;

	@Autowired
	private RedisService redisService;

	private NimbusJwtDecoder nimbusJwtDecoder;

	@PostConstruct
	public void init() {
		SecretKey key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
		this.nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(key)
				.macAlgorithm(MacAlgorithm.HS512)
				.build();
	}

	@Override
	public Jwt decode(String token) throws JwtException {
		// 1. Perform custom validation first (e.g., check revocation list)
		if (redisService.isBlackListed(token)) {
			throw new JwtException("Token has been revoked");
		}
		try {
			// 2. If not revoked, delegate to the standard decoder for signature and expiration validation
			return nimbusJwtDecoder.decode(token);
		} catch (JwtException e) {
			// Re-throw specific JWT exceptions
			throw e;
		} catch (Exception e) { // Catch other potential unexpected errors
			throw new JwtException("Invalid token", e);
		}
	}

}
