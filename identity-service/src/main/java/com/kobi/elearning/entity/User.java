package com.kobi.elearning.entity;

import com.kobi.elearning.constant.AuthProvider;
import com.kobi.elearning.constant.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

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
    Instant createdAt;
    @UpdateTimestamp
    Instant updatedAt;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    Status status = Status.ACTIVE;
}
