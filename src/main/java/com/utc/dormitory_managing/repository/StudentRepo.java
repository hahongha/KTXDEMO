package com.utc.dormitory_managing.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utc.dormitory_managing.entity.Student;

public interface StudentRepo extends JpaRepository<Student, String> {
	Optional<Student> findByStudentEmail(@Param("x") String email);
}
