package com.example.kafka.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;

public class BoardConsumer {
    private static final String TOPIC = "demo-topic";
    ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = TOPIC)
    public void listen(String message) {
        try {
            // Deserialize the message to a DemoEntity object
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
