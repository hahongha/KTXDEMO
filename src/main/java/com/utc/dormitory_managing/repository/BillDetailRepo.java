package com.utc.dormitory_managing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utc.dormitory_managing.entity.Bill;
import com.utc.dormitory_managing.entity.BillDetail;

public interface BillDetailRepo extends JpaRepository<BillDetail, Long>  {
	@Query("SELECT a from BillDetail a where a.bill.billId = :x ")
	List<BillDetail> findByBill(@Param("x") String billId);
}
