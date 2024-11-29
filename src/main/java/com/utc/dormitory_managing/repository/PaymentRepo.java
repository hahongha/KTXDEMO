package com.utc.dormitory_managing.repository;

import com.utc.dormitory_managing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
}