package com.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessKafkaEvent {

    private String transactionId;
    private BigDecimal amount;
    private String paymentType;
    private String email;
}