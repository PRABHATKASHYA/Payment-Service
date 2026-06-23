package com.payment_service.strategy;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;

public interface PaymentStrategy {
    PaymentResponse pay(PaymentRequest request);
}
