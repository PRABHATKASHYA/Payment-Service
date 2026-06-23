package com.payment_service.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class PaymentResponse {
    private String transactionId;

    private String status;

    private BigDecimal amount;

}
