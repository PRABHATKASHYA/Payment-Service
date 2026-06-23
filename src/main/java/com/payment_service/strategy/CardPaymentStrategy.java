package com.payment_service.strategy;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import org.springframework.stereotype.Service;

@Service
public class CardPaymentStrategy implements PaymentStrategy{
    @Override
    public PaymentResponse pay(PaymentRequest request){
        return PaymentResponse.builder()
                .transactionId("CARD123456")
                .status("SUCCESS")
                .amount(request.getAmount())
                .build();
    }
}
