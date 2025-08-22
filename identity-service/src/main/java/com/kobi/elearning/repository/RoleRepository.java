package com.kobi.elearning.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kobi.elearning.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
	Role findByName(String name);

	boolean existsByName(String name);
}
