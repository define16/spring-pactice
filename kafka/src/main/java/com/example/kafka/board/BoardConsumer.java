package com.example.kafka.board;

import com.example.kafka.model.DemoCreatedMessage;
import com.example.kafka.utils.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.example.kafka.config.KafkaConfig.TOPIC_DEMO;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardConsumer {

    private final BoardService boardService;

    @KafkaListener(topics = TOPIC_DEMO, groupId = "demo-group")
    public void listen(String message) {
        try {
            // Deserialize the message to a DemoEntity object
            log.info("Received message: {}", message);
            DemoCreatedMessage msg = JsonUtil.fromJson(message, DemoCreatedMessage.class);
            boardService.createDemo(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
