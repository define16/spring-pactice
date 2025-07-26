package com.example.kafka.board;

import com.example.kafka.board.dto.PushMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board/{id}")
    @ResponseBody
    public String getBoard(@PathVariable Long id) throws Exception {
        return boardService.getBoardHeadline(id);
    }

    @SubscriptionMapping("boardSubscription")
    public Flux<PushMessage> subscribe(@Argument Long boardId) {
        log.info("Subscribing to boardId: {}", boardId);
        return boardService.subscribe(boardId);
    }
}
