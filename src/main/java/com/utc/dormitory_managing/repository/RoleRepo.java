package com.utc.dormitory_managing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.utc.dormitory_managing.entity.Role;

public interface RoleRepo  extends JpaRepository<Role, String> {
	Optional<Role> findByRoleName(@Param("x") String roleName);
}
