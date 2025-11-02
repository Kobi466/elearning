package com.kobi.elearning.service.implement;


import com.kobi.elearning.constant.AuthProvider;
import com.kobi.elearning.constant.PredefinedRole;
import com.kobi.elearning.dto.request.PasswordCreationRequest;
import com.kobi.elearning.dto.request.UserCreateRequest;
import com.kobi.elearning.dto.request.UserUpdateRequest;
import com.kobi.elearning.dto.response.UserResponse;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;

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
//		var id = ((org.springframework.security.oauth2.jwt.Jwt) context.getAuthentication().getPrincipal()).getSubject();
        var id = context.getAuthentication().getName();
        User user1 = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (StringUtils.hasText(user1.getPasswordHash())
                || user1.getProvider() == AuthProvider.LOCAL) {
            throw new AppException(ErrorCode.PASSWORD_EXISTED);
        }
        user1.setPasswordHash(passwordEncoder.encode(request.getPassWord()));
        userRepository.save(user1);
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        request.setPassWord(passwordEncoder.encode(request.getPassWord()));

        var user = userMapper.toUser(request);
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(PredefinedRole.STUDENT));
        user.setRoles(roles);

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
//		String userId = ((org.springframework.security.oauth2.jwt.Jwt) authentication.getAuthentication().getPrincipal()).getSubject();
        String userId = authentication.getAuthentication().getName();
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

