package com.kobi.elearning.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.kobi.elearning.dto.request.auth.PasswordCreationRequest;
import com.kobi.elearning.dto.request.profile.UserCreateRequest;
import com.kobi.elearning.dto.request.profile.UserUpdateRequest;
import com.kobi.elearning.dto.response.profile.UserResponse;

@Service
public interface UserService {
	UserResponse createUser(UserCreateRequest request);

	UserResponse updateUser(String id, UserUpdateRequest request);

	UserResponse getMyInformation();

	@PreAuthorize("hasRole('ADMIN')")
	List<UserResponse> getUsers();

	void createPassword(PasswordCreationRequest request);
}
