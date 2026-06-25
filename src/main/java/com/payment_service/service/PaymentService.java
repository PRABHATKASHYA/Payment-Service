package com.payment_service.service;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import com.payment_service.dto.PaymentSuccessKafkaEvent;
import com.payment_service.factory.PaymentFactory;
import com.payment_service.kafka.PaymentKafkaProducer;
import com.payment_service.strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentFactory paymentFactory;
    private final PaymentKafkaProducer paymentKafkaProducer;

    public PaymentResponse makePayment(PaymentRequest request) {

        PaymentStrategy paymentStrategy =
                paymentFactory.getPaymentStrategy(
                        request.getPaymentType()
                );

        PaymentResponse response =
                paymentStrategy.pay(request);

        PaymentSuccessKafkaEvent event =
                PaymentSuccessKafkaEvent.builder()
                        .transactionId(response.getTransactionId())
                        .amount(response.getAmount())
                        .paymentType(request.getPaymentType())
                        .email(request.getEmail())
                        .build();

        paymentKafkaProducer.publish(event);

        return response;
    }
}