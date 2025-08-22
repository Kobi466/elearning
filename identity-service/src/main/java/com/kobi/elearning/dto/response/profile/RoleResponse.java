package com.kobi.elearning.dto.response.profile;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
	String name;
	String description;
	Set<PermissionResponse> permissions;
}
