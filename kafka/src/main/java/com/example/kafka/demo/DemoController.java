package com.example.kafka.demo;

import com.example.kafka.demo.dto.CreateDemoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DemoController {
    private final DemoService demoService;

    @GetMapping("/demo/{id}")
    public String getDemo(@PathVariable Long id) throws Exception {
        return demoService.getDemoMessage(id);
    }


    @PostMapping("/demo")
    public Long createDemo(@RequestBody CreateDemoDto createDemoDto) throws Exception {
        return demoService.createDemo(createDemoDto);
    }
}
