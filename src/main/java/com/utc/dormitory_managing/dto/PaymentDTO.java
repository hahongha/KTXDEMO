package com.utc.dormitory_managing.dto;

import lombok.Builder;

public abstract class PaymentDTO {


    @Builder
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}
