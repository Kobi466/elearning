package com.kobi.elearning.entity;


import java.util.Date;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class RefreshToken {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(nullable = false, unique = true, length = 1000)
	String token;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	User user;
	@Column(nullable = false)
	Date expiryTime;
	@Column(nullable = false)
	boolean revoked = false;
	@Column(nullable = false)
	boolean used;
	@Column(nullable = false)
	Date createdAt;
}
