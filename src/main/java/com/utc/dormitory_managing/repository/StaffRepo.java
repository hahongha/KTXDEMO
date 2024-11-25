package com.utc.dormitory_managing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utc.dormitory_managing.entity.Staff;

public interface StaffRepo extends JpaRepository<Staff, String> {
}
