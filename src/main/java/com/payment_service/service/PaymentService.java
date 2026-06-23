package com.payment_service.service;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import com.payment_service.factory.PaymentFactory;
import com.payment_service.strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentFactory paymentFactory;

    public PaymentResponse makePayment(PaymentRequest request){
        PaymentStrategy paymentStrategy=paymentFactory.getPaymentStrategy(request.getPaymentType());
        return paymentStrategy.pay(request);
    }

}
