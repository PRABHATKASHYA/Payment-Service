package com.payment_service.service;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import com.payment_service.dto.PaymentSuccessKafkaEvent;
import com.payment_service.factory.PaymentFactory;
import com.payment_service.kafka.PaymentKafkaProducer;
import com.payment_service.strategy.PaymentStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentFactory paymentFactory;

    @Mock
    private PaymentKafkaProducer paymentKafkaProducer;

    @Mock
    private PaymentStrategy paymentStrategy;

    @InjectMocks
    private PaymentService paymentService;

    private PaymentRequest paymentRequest;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(new BigDecimal("100.00"));
        paymentRequest.setPaymentType("UPI");
        paymentRequest.setEmail("test@example.com");

        paymentResponse = PaymentResponse.builder()
                .transactionId("UPI123456")
                .status("SUCCESS")
                .amount(new BigDecimal("100.00"))
                .build();
    }

    @Test
    void makePayment_Success() {
        when(paymentFactory.getPaymentStrategy("UPI")).thenReturn(paymentStrategy);
        when(paymentStrategy.pay(paymentRequest)).thenReturn(paymentResponse);

        PaymentResponse result = paymentService.makePayment(paymentRequest);

        verify(paymentFactory).getPaymentStrategy("UPI");
        verify(paymentStrategy).pay(paymentRequest);
        verify(paymentKafkaProducer).publish(any(PaymentSuccessKafkaEvent.class));
        
        assert result.getTransactionId().equals("UPI123456");
        assert result.getStatus().equals("SUCCESS");
        assert result.getAmount().compareTo(new BigDecimal("100.00")) == 0;
    }

    @Test
    void makePayment_CardPayment() {
        paymentRequest.setPaymentType("CARD");
        paymentResponse.setTransactionId("CARD123456");

        when(paymentFactory.getPaymentStrategy("CARD")).thenReturn(paymentStrategy);
        when(paymentStrategy.pay(paymentRequest)).thenReturn(paymentResponse);

        PaymentResponse result = paymentService.makePayment(paymentRequest);

        verify(paymentFactory).getPaymentStrategy("CARD");
        verify(paymentStrategy).pay(paymentRequest);
        verify(paymentKafkaProducer).publish(any(PaymentSuccessKafkaEvent.class));
        
        assert result.getTransactionId().equals("CARD123456");
    }

    @Test
    void makePayment_KafkaEventPublished() {
        when(paymentFactory.getPaymentStrategy("UPI")).thenReturn(paymentStrategy);
        when(paymentStrategy.pay(paymentRequest)).thenReturn(paymentResponse);

        paymentService.makePayment(paymentRequest);

        verify(paymentKafkaProducer, times(1)).publish(any(PaymentSuccessKafkaEvent.class));
    }

    @Test
    void makePayment_KafkaEventContainsCorrectData() {
        when(paymentFactory.getPaymentStrategy("UPI")).thenReturn(paymentStrategy);
        when(paymentStrategy.pay(paymentRequest)).thenReturn(paymentResponse);

        paymentService.makePayment(paymentRequest);

        verify(paymentKafkaProducer).publish(argThat(event -> 
            event.getTransactionId().equals("UPI123456") &&
            event.getAmount().compareTo(new BigDecimal("100.00")) == 0 &&
            event.getPaymentType().equals("UPI") &&
            event.getEmail().equals("test@example.com")
        ));
    }
}
