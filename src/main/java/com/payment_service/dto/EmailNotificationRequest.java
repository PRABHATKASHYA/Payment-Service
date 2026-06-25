package com.payment_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailNotificationRequest {
    private String to;
    private String subject;
    private String message;
}
