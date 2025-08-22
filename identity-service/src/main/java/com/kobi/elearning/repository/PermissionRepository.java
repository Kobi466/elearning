package com.kobi.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kobi.elearning.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
