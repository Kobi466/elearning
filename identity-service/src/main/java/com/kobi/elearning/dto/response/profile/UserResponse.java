package com.kobi.elearning.dto.response.profile;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
	String id;
	String userName;
	String fullName;
	Boolean oauth2Account;
	Set<RoleResponse> roles;
}
