package com.payment_service.event;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class PaymentSuccessEvent {
    private final String transactionId;
    private final BigDecimal amount;
    private final String paymentType;
    private final String email;

}
