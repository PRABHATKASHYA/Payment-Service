package com.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class PaymentSuccessKafkaEvent {

    private String transactionId;
    private BigDecimal amount;
    private String paymentType;
    private String email;
}
