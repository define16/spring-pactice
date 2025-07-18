package com.example.kafka.demo;

import com.example.kafka.model.KafkaMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DemoProducer {
    private static final String TOPIC_NAME = "demo-topic";
    private final KafkaTemplate<String, KafkaMessage> kafkaMessageKafkaTemplate;

    public void send(KafkaMessage message) {
        kafkaMessageKafkaTemplate.send(TOPIC_NAME, message.getId(), message);
    }

    public void sendWithCallback(KafkaMessage message) {
        CompletableFuture<SendResult<String, KafkaMessage>> future = kafkaMessageKafkaTemplate.send(TOPIC_NAME, message.getId(), message);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                System.err.println("Error sending message: " + ex.getMessage());
            } else {
                System.out.println("Message sent successfully: " + result.getProducerRecord().value());
            }
        });
    }

}
