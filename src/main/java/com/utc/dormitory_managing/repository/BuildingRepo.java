package com.utc.dormitory_managing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utc.dormitory_managing.entity.Building;

@Repository
public interface BuildingRepo extends JpaRepository<Building, String> {

}
