package com.kobi.elearning.mapper;

import com.kobi.elearning.dto.response.RoleResponse;
import com.kobi.elearning.entity.Role;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
	Role toRole(RoleResponse response);
	RoleResponse toRoleResponse(Role role);
}
