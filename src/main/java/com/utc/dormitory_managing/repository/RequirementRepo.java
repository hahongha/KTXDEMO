package com.utc.dormitory_managing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utc.dormitory_managing.entity.Requirement;


public interface RequirementRepo extends JpaRepository<Requirement, String>  {

}


