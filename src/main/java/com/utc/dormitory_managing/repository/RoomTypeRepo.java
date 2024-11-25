package com.utc.dormitory_managing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utc.dormitory_managing.entity.RoomType;

public interface RoomTypeRepo extends JpaRepository<RoomType, String>  {

}
