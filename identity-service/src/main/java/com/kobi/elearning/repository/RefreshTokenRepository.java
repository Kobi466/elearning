package com.kobi.elearning.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kobi.elearning.entity.RefreshToken;
import com.kobi.elearning.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	RefreshToken findByUser(Optional<User> user);

	RefreshToken findByToken(String token);

	List<RefreshToken> findAllByUser(User user);

	@Transactional
	@Modifying
	@Query("""
			delete from RefreshToken r where
				r.expiryTime < :now
				or r.used = true
				or r.revoked = true
			""")
	int deleteExpiredOrUsedOrRevokedTokens(@Param("now") Date now);
}
