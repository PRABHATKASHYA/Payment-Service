package com.payment_service.strategy;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CardPaymentStrategyTest {

    private CardPaymentStrategy cardPaymentStrategy;
    private PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        cardPaymentStrategy = new CardPaymentStrategy();
        paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(new BigDecimal("100.00"));
        paymentRequest.setPaymentType("CARD");
        paymentRequest.setEmail("test@example.com");
    }

    @Test
    void pay_ReturnsSuccessResponse() {
        PaymentResponse response = cardPaymentStrategy.pay(paymentRequest);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("CARD123456", response.getTransactionId());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
    }

    @Test
    void pay_WithDifferentAmount_ReturnsCorrectAmount() {
        paymentRequest.setAmount(new BigDecimal("500.50"));

        PaymentResponse response = cardPaymentStrategy.pay(paymentRequest);

        assertEquals(new BigDecimal("500.50"), response.getAmount());
    }

    @Test
    void pay_TransactionIdIsFixed() {
        PaymentResponse response = cardPaymentStrategy.pay(paymentRequest);

        assertEquals("CARD123456", response.getTransactionId());
    }

    @Test
    void pay_StatusIsAlwaysSuccess() {
        PaymentResponse response = cardPaymentStrategy.pay(paymentRequest);

        assertEquals("SUCCESS", response.getStatus());
    }
}
