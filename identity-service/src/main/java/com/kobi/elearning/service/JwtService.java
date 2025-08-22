package com.kobi.elearning.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.kobi.elearning.entity.User;


@Service
public interface JwtService {
	String generateAccessToken(User user);

	String generateRefreshToken(User user);

	boolean validateToken(String token);

	String getIdFromToken(String token);

	Date getExpirationDateFromToken(String token);

	Date getIssuedAtDateFromToken(String token);

	long getExpirationTimeFromToken(String token);
}
