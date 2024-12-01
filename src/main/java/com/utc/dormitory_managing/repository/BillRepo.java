package com.utc.dormitory_managing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utc.dormitory_managing.entity.Bill;

public interface BillRepo extends JpaRepository<Bill, String>  {
	@Query("SELECT a from Bill a where a.billStatus = :x")
	List<Bill> findBillByStatus(@Param("x") String status);
}
