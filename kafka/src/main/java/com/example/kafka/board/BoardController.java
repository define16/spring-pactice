package com.example.kafka.board;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board/{id}")
    public String getBoard(@PathVariable Long id) throws Exception {
        return boardService.getBoardHeadline(id);
    }
}
