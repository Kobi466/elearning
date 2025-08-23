package com.kobi.elearning.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userId;
	String userName;
    String email;
    Boolean emailVerified;
    Boolean oauth2Account;
    String provider;
    String status;
    Instant createdAt;
}
