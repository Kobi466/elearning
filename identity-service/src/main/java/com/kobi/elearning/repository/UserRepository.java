package com.kobi.elearning.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kobi.elearning.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	boolean existsByUserName(String userName);

	Optional<User> findByUserName(String userName);

	Optional<User> findById(String id);
}
