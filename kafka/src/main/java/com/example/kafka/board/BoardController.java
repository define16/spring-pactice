package com.example.kafka.board;

import com.example.kafka.demo.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final DemoService demoService;

    @GetMapping("/demo")
    public String demoEndpoint(Long id) throws Exception {
        return demoService.getDemoMessage(id);
    }
}
