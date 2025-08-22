package com.kobi.elearning.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String id;
	@Column(unique = true, nullable = false)
	String userName;
	String passWord;
	String fullName;
	@ManyToMany
	Set<Role> roles;
	@Column(nullable = false)
	@Builder.Default
	Boolean oauth2Account = false;
}
