package com.kobi.elearning.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.kobi.elearning.dto.request.profile.UserCreateRequest;
import com.kobi.elearning.dto.request.profile.UserUpdateRequest;
import com.kobi.elearning.dto.response.profile.UserResponse;
import com.kobi.elearning.entity.User;


@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
	@Mapping(target = "roles", ignore = true)
	User toUser(UserCreateRequest request);

	UserResponse toUserResponse(User user);
	@Mapping(target =  "roles", ignore = true)
	void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
