package com.payment_service.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotNull (message =" Amount is required")
    @Positive(message = "Amount is greater then 0")
    private BigDecimal amount;

    @NotBlank(message = "Payment type is required")
    private String paymentType;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;
}
