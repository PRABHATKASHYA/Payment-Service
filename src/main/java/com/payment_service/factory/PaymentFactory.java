package com.payment_service.factory;

import com.payment_service.exception.InvalidPaymentTypeException;
import com.payment_service.strategy.CardPaymentStrategy;
import com.payment_service.strategy.PaymentStrategy;
import com.payment_service.strategy.UpiPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFactory {

    private final UpiPaymentStrategy upiPaymentStrategy;
    private final CardPaymentStrategy cardPaymentStrategy;


    public PaymentStrategy getPaymentStrategy(String paymentType){

//        if (paymentType == null || paymentType.isBlank()) {
//            throw new RuntimeException(
//                    "paymentType is required. Use UPI or CARD"
//            );
//        }

        if (paymentType.equalsIgnoreCase("UPI")){
            return upiPaymentStrategy;
        }
        if (paymentType.equalsIgnoreCase("CARD")){
            return cardPaymentStrategy;
        }

        throw new InvalidPaymentTypeException("Invalid payment type. Use UPI or CARD");
    }
}
