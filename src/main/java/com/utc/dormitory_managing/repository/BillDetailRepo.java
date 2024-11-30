package com.utc.dormitory_managing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utc.dormitory_managing.entity.Bill;
import com.utc.dormitory_managing.entity.BillDetail;

public interface BillDetailRepo extends JpaRepository<BillDetail, Long>  {

}
