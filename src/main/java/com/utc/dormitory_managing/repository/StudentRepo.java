package com.utc.dormitory_managing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utc.dormitory_managing.entity.Student;

public interface StudentRepo extends JpaRepository<Student, String> {
	@Query("SELECT s FROM Student s WHERE s.studentEmail = :x")
	Optional<Student> findByStudentEmail(@Param("x") String email);
	
	@Query("SELECT Count(a.studentId) from Student a where a.room.roomId = :x and a.studentStatus = 0")
	Long getStudentNumber(@Param("x") String roomId);
	
	@Query("SELECT a from Student a where a.room.roomId = :x and a.studentStatus = 0")
	List<Student> findByRoom(@Param("x") String roomId);
	
	@Query("SELECT a from Student a where a.user.userId = :x")
	Optional<Student> findByUser(@Param("x") String userId);
	
	
}
