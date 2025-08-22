package com.kobi.elearning.controller;


import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kobi.elearning.dto.request.auth.PasswordCreationRequest;
import com.kobi.elearning.dto.request.profile.UserCreateRequest;
import com.kobi.elearning.dto.request.profile.UserUpdateRequest;
import com.kobi.elearning.dto.response.ApiResponse;
import com.kobi.elearning.dto.response.profile.UserResponse;
import com.kobi.elearning.exception.SuccessCode;
import com.kobi.elearning.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class UserController {
	UserService userService;
	@PostMapping("/register")
	ApiResponse<UserResponse> register(@Valid @RequestBody UserCreateRequest request) {
		return ApiResponse.ok(userService.createUser(request), SuccessCode.USER_CREATED);
	}
	@PutMapping("/{id}")
	public ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
		return ApiResponse.ok( userService.updateUser(id, request), SuccessCode.USER_UPDATED);
	}

	@GetMapping("/me")
	public ApiResponse<UserResponse> getUser() {
		return ApiResponse.ok(userService.getMyInformation(), SuccessCode.USER_CREATED);
	}

	@GetMapping
	public ApiResponse<List<UserResponse>> getUsers() {
		return ApiResponse.ok(userService.getUsers(), SuccessCode.USER_FETCH_SUCCESS);
	}
	@PostMapping("creation-password")
	public ApiResponse<Void> createPassword(@Valid @RequestBody PasswordCreationRequest request){
		userService.createPassword(request);
		return ApiResponse.ok(null, SuccessCode.CREATION_PASSWORD_SUCCESS);
	}
}
