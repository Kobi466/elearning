package com.kobi.elearning.mapper;

import org.mapstruct.Mapper;

import com.kobi.elearning.dto.response.PermissionResponse;
import com.kobi.elearning.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
	Permission toPermission(PermissionResponse response);
	PermissionResponse toRoleResponse(Permission role);
}
