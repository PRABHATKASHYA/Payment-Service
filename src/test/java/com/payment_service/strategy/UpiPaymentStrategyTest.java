package com.payment_service.strategy;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UpiPaymentStrategyTest {

    private UpiPaymentStrategy upiPaymentStrategy;
    private PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        upiPaymentStrategy = new UpiPaymentStrategy();
        paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(new BigDecimal("100.00"));
        paymentRequest.setPaymentType("UPI");
        paymentRequest.setEmail("test@example.com");
    }

    @Test
    void pay_ReturnsSuccessResponse() {
        PaymentResponse response = upiPaymentStrategy.pay(paymentRequest);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("UPI123456", response.getTransactionId());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
    }

    @Test
    void pay_WithDifferentAmount_ReturnsCorrectAmount() {
        paymentRequest.setAmount(new BigDecimal("750.25"));

        PaymentResponse response = upiPaymentStrategy.pay(paymentRequest);

        assertEquals(new BigDecimal("750.25"), response.getAmount());
    }

    @Test
    void pay_TransactionIdIsFixed() {
        PaymentResponse response = upiPaymentStrategy.pay(paymentRequest);

        assertEquals("UPI123456", response.getTransactionId());
    }

    @Test
    void pay_StatusIsAlwaysSuccess() {
        PaymentResponse response = upiPaymentStrategy.pay(paymentRequest);

        assertEquals("SUCCESS", response.getStatus());
    }
}
