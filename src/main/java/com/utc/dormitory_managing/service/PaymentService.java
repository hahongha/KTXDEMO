package com.utc.dormitory_managing.service;
import com.utc.dormitory_managing.configuration.VNPAYConfig;
import com.utc.dormitory_managing.dto.*;
import com.utc.dormitory_managing.entity.Payment;
import com.utc.dormitory_managing.repository.PaymentRepo;
import com.utc.dormitory_managing.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService  {
    private final PaymentRepo paymentRepository;
    private final HttpServletRequest httpServletRequest;
    private final VNPAYConfig vnPayConfig;

    public Payment savePaymentResult(String amount, String orderInfo) {

        Payment payment = Payment.builder()
                .amount(amount)
                .paymentInfo(orderInfo)
                .paymentTime(LocalDateTime.now())
                .status("SUCCESS")
                .build();
        return paymentRepository.save(payment);
    }

   public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request, String amount, String studentId,String billId ) {
        long amount1 = Long.parseLong(amount)*100L;
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount1));
        vnpParamsMap.put("vnp_OrderInfo", studentId+"_"+billId);
        vnpParamsMap.put("vnp_BankCode", "NCB");
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentDTO.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }
    // Get all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Get payment by id
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    // Delete payment
    public boolean deletePayment(Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<Payment> searchPaymentTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return paymentRepository.searchByTerm(searchTerm);
    }
}
