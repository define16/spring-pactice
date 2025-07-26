package com.example.kafka.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.example.kafka.config.KafkaConfig.TOPIC_DEMO;

@Slf4j
@Service
public class BoardConsumer {
    ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = TOPIC_DEMO, groupId = "demo-group")
    public void listen(String message) {
        try {
            // Deserialize the message to a DemoEntity object
            log.info("Received message: {}", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
