package com.kobi.elearning.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisService {
	RedisTemplate<String, Object> redisTemplate;
	public void blackListAccessToken(String token, long expireTime) {
		String key = "access_token:" + token;
		redisTemplate.opsForValue().set(key, "logout", expireTime, TimeUnit.MILLISECONDS);
	}
	public boolean isBlackListed(String token) {
		String key = "access_token:" + token;
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
}
