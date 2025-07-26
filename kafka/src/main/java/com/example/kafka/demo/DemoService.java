package com.example.kafka.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final DemoRepository demoRepository;

    public String getDemoMessage(Long id) throws Exception {

        return demoRepository.findById(id).map(DemoEntity::getContent).orElseThrow(() -> new NoSuchElementException("DemoEntity not found with id: " + id));
    }
}
