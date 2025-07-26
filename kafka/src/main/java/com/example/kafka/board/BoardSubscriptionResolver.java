package com.example.kafka.board;

import com.example.kafka.board.dto.PushMessage;
import com.example.kafka.board.dto.PushMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardSubscriptionResolver {

    private final Sinks.Many<PushMessage> sink;

    /**
     * 클라이언트로 푸시할 메시지를 발행
     */
    public void publish(PushMessageDto pushMessageDto) {
        log.info("publishing to boardId: {}", pushMessageDto.getId());

        PushMessage msg = new PushMessage(
                pushMessageDto.getId(), pushMessageDto.getHeadline(),pushMessageDto.getSavedAt().atZone(ZoneId.systemDefault()).toOffsetDateTime()
        );
        sink.tryEmitNext(msg);
        int nSubscribers = sink.currentSubscriberCount();
        log.info("현재 Push 수: {}", nSubscribers);
    }

    /**
     * 구독
     */
    public Flux<PushMessage> subscribe(Long boardId) {
        log.info("Subscribing to boardId: {}", boardId);
        return sink.asFlux().filter(msg ->
            msg.getId().equals(boardId)
        );
    }
}
