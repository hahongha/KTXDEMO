package com.utc.dormitory_managing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utc.dormitory_managing.entity.Clock;
import com.utc.dormitory_managing.entity.Room;

public interface ClockRepo extends JpaRepository<Clock, String>  {
}
