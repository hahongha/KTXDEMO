package com.utc.dormitory_managing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utc.dormitory_managing.entity.Floor;

public interface FloorRepo extends JpaRepository<Floor, String>  {

}
