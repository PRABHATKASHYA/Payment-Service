package com.payment_service.kafka;

import com.payment_service.dto.PaymentSuccessKafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentKafkaProducer {

    private final KafkaTemplate<String, PaymentSuccessKafkaEvent> kafkaTemplate;

    @Value("${payment.kafka.topic.payment-success}")
    private String paymentSuccessTopic;

    public void publish(PaymentSuccessKafkaEvent event) {
        try {
            kafkaTemplate.send(paymentSuccessTopic, event.getTransactionId(), event);
            log.info("Payment success event published to Kafka: {}", event.getTransactionId());
        } catch (Exception e) {
            log.error("Error publishing payment success event to Kafka", e);
            throw new RuntimeException("Failed to publish payment event to Kafka", e);
        }
    }
}
