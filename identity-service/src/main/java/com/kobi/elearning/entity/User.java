package com.kobi.elearning.entity;

import java.time.Instant;
import java.util.Set;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.kobi.elearning.constant.AuthProvider;
import com.kobi.elearning.constant.Status;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = {"provider", "provider_id"})
})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String userId;
	@Column(unique = true, nullable = false)
	String userName;
	String email;
	String passwordHash;
	boolean emailVerified;
	@Enumerated(EnumType.STRING)
	@Builder.Default
	AuthProvider provider = AuthProvider.LOCAL;
	String providerId;
	@ManyToMany
	Set<Role> roles;
	@Column(nullable = false)
	@Builder.Default
	Boolean oauth2Account = false;
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt = Instant.now();  // Giá trị mặc định

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Instant updatedAt;
	@Enumerated(EnumType.STRING)
	@Builder.Default
	Status status = Status.ACTIVE;
	@PrePersist
	protected void onCreate() {
		if (createdAt == null) {
			createdAt = Instant.now();
		}
	}
}
