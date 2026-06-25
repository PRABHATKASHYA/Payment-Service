package com.payment_service.listener;

import com.payment_service.event.PaymentSuccessEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentAuditListener {

    @EventListener
    public void savePaymentAudit(PaymentSuccessEvent event){
        System.out.println(
                "Payment audit saved for transaction: "
                        + event.getTransactionId()
        );
        System.out.println( "Transaction Id: "
                + event.getTransactionId());

        System.out.println("Amount: "
                + event.getAmount()
        );
    }
}
