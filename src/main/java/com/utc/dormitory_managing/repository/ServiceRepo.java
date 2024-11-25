package com.utc.dormitory_managing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.utc.dormitory_managing.entity.Services;

public interface ServiceRepo extends JpaRepository<Services, String>  {
	
	Optional<Services> findByServiceName(@Param("x") String value);
}
