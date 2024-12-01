package com.utc.dormitory_managing.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.utc.dormitory_managing.entity.BillDetail;

public interface BillDetailService {
	@Query("Select a from BillDetail a where a.bill.billId = :x")
	List<BillDetail> findByBill(@Param("x") String billId);
}
