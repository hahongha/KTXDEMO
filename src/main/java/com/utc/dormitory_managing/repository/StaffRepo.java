package com.utc.dormitory_managing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utc.dormitory_managing.entity.Staff;

public interface StaffRepo extends JpaRepository<Staff, String> {
	@Query("SELECT a from Staff a where a.user.userId = :x")
	Optional<Staff> findByUser(@Param("x") String userId);
}
