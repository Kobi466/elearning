package com.kobi.elearning.mapper;

import com.kobi.elearning.dto.request.UserCreateRequest;
import com.kobi.elearning.dto.request.UserUpdateRequest;
import com.kobi.elearning.dto.response.UserResponse;
import com.kobi.elearning.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
    @Mapping(target = "passwordHash", source = "passWord")
	User toUser(UserCreateRequest request);

	UserResponse toUserResponse(User user);
	@Mapping(target =  "roles", ignore = true)
	void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
