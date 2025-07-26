package com.example.kafka.board;

import com.example.kafka.board.dto.PushMessage;
import com.example.kafka.board.dto.PushMessageDto;
import com.example.kafka.model.DemoCreatedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardSubscriptionResolver boardSubscriptionResolver;

    public String getBoardHeadline(Long id) throws Exception {
        return boardRepository.findById(id).map(BoardEntity::getHeadline).orElseThrow(() -> new Exception("BoardEntity not found with id: " + id));
    }

    @Transactional
    public void createDemo(DemoCreatedMessage msg) {
        BoardEntity boardEntity = boardRepository.findById(msg.getId()).map(board -> {
            board.setHeadline(msg.getHeadline());
            board.setSavedAt(LocalDateTime.now());
            return board;
        }).orElseGet(
            () -> new BoardEntity(msg.getId(), msg.getHeadline()));
        boardSubscriptionResolver.publish(new PushMessageDto(boardEntity.getId(), boardEntity.getHeadline(), boardEntity.getSavedAt()));
        boardRepository.save(boardEntity);
    }

    public Flux<PushMessage> subscribe(Long boardId) {
        return boardSubscriptionResolver.subscribe(boardId);
    }
}
