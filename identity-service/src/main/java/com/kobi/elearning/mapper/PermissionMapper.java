package com.kobi.elearning.mapper;

import com.kobi.elearning.dto.response.PermissionResponse;
import com.kobi.elearning.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
	Permission toPermission(PermissionResponse response);
	PermissionResponse toRoleResponse(Permission role);
}
