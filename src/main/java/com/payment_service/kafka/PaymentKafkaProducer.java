package com.payment_service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment_service.dto.PaymentSuccessKafkaEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${payment.kafka.topic.payment-success}")
    private String paymentSuccessTopic;

    public void publish(PaymentSuccessKafkaEvent event) {

        try {
            String jsonMessage =
                    objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    paymentSuccessTopic,
                    event.getTransactionId(),
                    jsonMessage
            ).whenComplete((result, exception) -> {

                if (exception != null) {
                    System.out.println(
                            "Kafka publish failed: "
                                    + exception.getMessage()
                    );
                    return;
                }

                System.out.println(
                        "Kafka event published successfully"
                );
            });

        } catch (JsonProcessingException exception) {
            throw new RuntimeException(
                    "Payment event JSON conversion failed",
                    exception
            );
        }
    }
}