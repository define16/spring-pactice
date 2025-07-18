package com.example.kafka.board;

import com.example.kafka.demo.DemoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository demoRepository;

    public String getDemoMessage(Long id) throws Exception {

        return demoRepository.findById(id).map(DemoEntity::getContent).orElseThrow(() -> new Exception("DemoEntity not found with id: " + id));
    }
}
