package com.kobi.elearning.mapper;

import org.mapstruct.Mapper;

import com.kobi.elearning.dto.response.profile.RoleResponse;
import com.kobi.elearning.entity.Role;

@Mapper (componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
	Role toRole(RoleResponse response);
	RoleResponse toRoleResponse(Role role);
}
