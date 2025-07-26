package com.example.kafka.board;

import com.example.kafka.model.DemoCreatedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    public String getBoardHeadline(Long id) throws Exception {
        return boardRepository.findById(id).map(BoardEntity::getHeadline).orElseThrow(() -> new Exception("BoardEntity not found with id: " + id));
    }

    @Transactional
    public void createDemo(DemoCreatedMessage msg) {
        BoardEntity boardEntity = new BoardEntity(msg.getId(), msg.getHeadline());
        boardRepository.save(boardEntity);
    }
}
