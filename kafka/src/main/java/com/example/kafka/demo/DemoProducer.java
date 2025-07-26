package com.example.kafka.demo;

import com.example.kafka.model.KafkaMessage;
import com.example.kafka.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.example.kafka.config.KafkaConfig.TOPIC_DEMO;

@Slf4j
@Service
@RequiredArgsConstructor
public class DemoProducer {
    private final KafkaTemplate<String, String> kafkaMessageKafkaTemplate;

    public void send(KafkaMessage message) {
        String msg = JsonUtil.toJson(message);
        this.sendWithCallback(msg);
    }

    private void sendWithCallback(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaMessageKafkaTemplate.send(TOPIC_DEMO, message);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Error sending message: {}", ex.getMessage());
            } else {
                log.error("Message sent successfully: {}", result.getProducerRecord().value());
            }
        });
    }

}
