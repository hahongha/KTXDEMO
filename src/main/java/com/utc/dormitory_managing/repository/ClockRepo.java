package com.utc.dormitory_managing.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.utc.dormitory_managing.entity.Clock;

public interface ClockRepo extends JpaRepository<Clock, String>  {
}
