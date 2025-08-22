package com.kobi.elearning.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
	@Id
	String name;
	String description;

	// Uncomment if you want to add permissions in the future
	@ManyToMany
	Set<Permission> permissions;
}
