package com.example.kafka.demo;

import com.example.kafka.demo.dto.CreateDemoDto;
import com.example.kafka.model.DemoCreatedMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DemoService {

    private final DemoRepository demoRepository;
    private final DemoProducer demoProducer;

    public String getDemoMessage(Long id) throws Exception {
        return demoRepository.findById(id).map(DemoEntity::getContent).orElseThrow(() -> new NoSuchElementException("DemoEntity not found with id: " + id));
    }

    @Transactional
    public Long createDemo(CreateDemoDto demoDto) {
        DemoEntity demoEntity = new DemoEntity(demoDto.getHeadline(), demoDto.getContent());
        DemoEntity savedEntity = demoRepository.save(demoEntity);
        demoProducer.send(new DemoCreatedMessage(
                savedEntity.getId(),
                savedEntity.getHeadline()
        ));
        return savedEntity.getId();
    }
}
