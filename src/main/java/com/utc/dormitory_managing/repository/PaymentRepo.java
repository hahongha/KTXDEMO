package com.utc.dormitory_managing.repository;

import com.utc.dormitory_managing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepo extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.paymentInfo LIKE %:searchTerm%")
    List<Payment> searchByTerm(@Param("searchTerm") String searchTerm);
}
