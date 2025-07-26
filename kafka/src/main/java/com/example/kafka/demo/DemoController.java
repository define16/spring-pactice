package com.example.kafka.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {
    private final DemoService demoService;

    @GetMapping("/demo/{id}")
    public String demoEndpoint(@PathVariable Long id) throws Exception {
        return demoService.getDemoMessage(id);
    }
}
