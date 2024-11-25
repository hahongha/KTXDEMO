package com.utc.dormitory_managing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utc.dormitory_managing.entity.Bill;

public interface BillRepo extends JpaRepository<Bill, String>  {

}
