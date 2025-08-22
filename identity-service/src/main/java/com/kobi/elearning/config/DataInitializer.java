package com.kobi.elearning.config;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kobi.elearning.constant.PredefinedRole;
import com.kobi.elearning.entity.*;
import com.kobi.elearning.entity.User;
import com.kobi.elearning.repository.PermissionRepository;
import com.kobi.elearning.repository.RoleRepository;
import com.kobi.elearning.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final PermissionRepository permissionRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		log.info("Starting application data initialization...");

		initializePermissions();
		initializeRoles();
		initializeAdminUser();

		log.info("Application data initialization completed.");
	}

	private void initializeRoles() {
		log.info("Checking and initializing roles...");

		Map<String, String> requiredRoles = Map.of(
				PredefinedRole.ADMIN, "Quản trị hệ thống",
				PredefinedRole.TEACHER, "Giáo viên",
				PredefinedRole.STUDENT, "Học viên",
				PredefinedRole.MENTOR, "Cố vấn"
		);

		Set<String> existingRoleNames = roleRepository.findAll().stream()
				.map(Role::getName)
				.collect(Collectors.toSet());

		List<Role> newRoles = new ArrayList<>();
		for (Map.Entry<String, String> entry : requiredRoles.entrySet()) {
			if (!existingRoleNames.contains(entry.getKey())) {
				newRoles.add(Role.builder()
						.name(entry.getKey())
						.description(entry.getValue())
						.build());
			}
		}

		if (!newRoles.isEmpty()) {
			log.info("Creating {} new roles: {}", newRoles.size(), newRoles.stream().map(Role::getName).collect(Collectors.joining(", ")));
			roleRepository.saveAll(newRoles);
		}
	}

	private void initializePermissions() {
		log.info("Checking and initializing permissions...");
		Map<String, String> requiredPermissions = Map.ofEntries(
				Map.entry("course:create", "Tạo khóa học mới"),
				Map.entry("course:read", "Xem thông tin khóa học"),
				Map.entry("course:update", "Cập nhật khóa học"),
				Map.entry("course:delete", "Xóa khóa học"),
				Map.entry("user:read", "Xem danh sách người dùng"),
				Map.entry("user:update", "Cập nhật thông tin người dùng"),
				Map.entry("user:delete", "Xóa người dùng"),
				Map.entry("role:assign", "Gán vai trò cho người dùng")
		);

		Set<String> existingPermissions = permissionRepository.findAll().stream()
				.map(Permission::getName)
				.collect(Collectors.toSet());

		List<Permission> newPermissions = requiredPermissions.entrySet().stream()
				.filter(entry -> !existingPermissions.contains(entry.getKey()))
				.map(entry -> Permission.builder()
						.name(entry.getKey())
						.description(entry.getValue())
						.build())
				.toList();

		if (!newPermissions.isEmpty()) {
			log.info("Creating {} new permissions...", newPermissions.size());
			permissionRepository.saveAll(newPermissions);
		}
	}

	private void initializeAdminUser() {
		log.info("Checking and initializing admin user...");
		String adminUsername = "admin";
		if (!userRepository.existsByUserName(adminUsername)) {
			User admin = new User();
			admin.setUserName(adminUsername);
			admin.setPassWord(passwordEncoder.encode("admin123"));
			admin.setFullName("Administrator");
			Role adminRole = roleRepository.findByName(PredefinedRole.ADMIN);
			admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
			userRepository.save(admin);
			log.info("Admin user '{}' created.", adminUsername);
		}
	}
}
