package com.example.kafka.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {
    private final DemoService demoService;

    @GetMapping("/demo")
    public String demoEndpoint(Long id) throws Exception {
        return demoService.getDemoMessage(id);
    }
}
