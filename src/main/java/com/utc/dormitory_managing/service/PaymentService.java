package com.utc.dormitory_managing.service;

import com.utc.dormitory_managing.configuration.VNPAYConfig;
import com.utc.dormitory_managing.dto.PaymentDTO;
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
    public Payment savePaymentResult(String amount, String paymentInfo) {
        Payment payment = Payment.builder()
//                .orderId(orderId)
                .amount(amount)
                .paymentInfo(paymentInfo)
                .paymentTime(LocalDateTime.now())
                .status("SUCCESS")
                .build();
        return paymentRepository.save(payment);
    }


    private final VNPAYConfig vnPayConfig;
    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request) {
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
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
}
