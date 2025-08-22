package com.kobi.elearning.service;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kobi.elearning.repository.RefreshTokenRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenClearAuto {
	RefreshTokenRepository refreshTokenRepository;
	@Scheduled(cron = "0 15 * * * ?") // chạy mỗi ngày lúc 00:00
	public void cleanUpTokens() {
		int deletedCount = refreshTokenRepository.deleteExpiredOrUsedOrRevokedTokens(new Date());
		log.info("Cleaned up {} expired / used / revoked refresh tokens.", deletedCount);
	}
}
