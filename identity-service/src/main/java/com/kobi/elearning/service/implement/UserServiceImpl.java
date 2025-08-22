package com.kobi.elearning.service.implement;


import java.util.HashSet;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.kobi.elearning.constant.PredefinedRole;
import com.kobi.elearning.dto.request.auth.PasswordCreationRequest;
import com.kobi.elearning.dto.request.profile.UserCreateRequest;
import com.kobi.elearning.dto.request.profile.UserUpdateRequest;
import com.kobi.elearning.dto.response.profile.UserResponse;
import com.kobi.elearning.entity.Role;
import com.kobi.elearning.entity.User;
import com.kobi.elearning.exception.AppException;
import com.kobi.elearning.exception.ErrorCode;
import com.kobi.elearning.mapper.UserMapper;
import com.kobi.elearning.repository.RoleRepository;
import com.kobi.elearning.repository.UserRepository;
import com.kobi.elearning.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
	UserRepository userRepository;
	UserMapper userMapper;
	RoleRepository roleRepository;
	PasswordEncoder passwordEncoder;


	@Override
	public void createPassword(PasswordCreationRequest request) {
		var context = SecurityContextHolder.getContext();
		var id = ((org.springframework.security.oauth2.jwt.Jwt) context.getAuthentication().getPrincipal()).getSubject();
		User user1 = userRepository.findById(id)
				.orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
		if (StringUtils.hasText(user1.getPassWord()))
			throw new AppException(ErrorCode.PASSWORD_EXISTED);
		user1.setPassWord(passwordEncoder.encode(request.getPassWord()));
		user1.setOauth2Account(false);
		userRepository.save(user1);
	}
	@Override
	public UserResponse createUser(UserCreateRequest request) {
		if (userRepository.existsByUserName(request.getUserName())) {
			throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
		}
		request.setPassWord(passwordEncoder.encode(request.getPassWord()));

		var user = userMapper.toUser(request);
		var roles = roleRepository.findAllById(request.getRoles().stream().toList());
		user.setRoles(new HashSet<>(roles));

		return userMapper.toUserResponse(userRepository.save(user));
	}

	@Override
	public UserResponse updateUser(String id, UserUpdateRequest request) {
		User user = getUser(id);
		if (userRepository.existsByUserName((request.getUserName()))) {
			log.error("User with username {} already exists", request.getUserName());
			throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
		}

		var roles = roleRepository.findAllById(request.getRoles());
		user.setRoles(new HashSet<>(roles));

		userMapper.updateUser(user, request);
		return userMapper.toUserResponse(userRepository.save(user));
	}

	private User getUser(String id) {
		if (userRepository.existsById(id)) {
			return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		} else {
			throw new AppException(ErrorCode.USER_NOT_FOUND);
		}
	}

	@Override
	public UserResponse getMyInformation() {
		var authentication = SecurityContextHolder.getContext();
		String userId = ((org.springframework.security.oauth2.jwt.Jwt) authentication.getAuthentication().getPrincipal()).getSubject();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		return userMapper.toUserResponse(user);
	}

	@Override
	public List<UserResponse> getUsers() {
		log.info("Fetching all users");
		return userRepository.findAll().stream()
				.map(userMapper::toUserResponse)
				.toList();
	}

}
