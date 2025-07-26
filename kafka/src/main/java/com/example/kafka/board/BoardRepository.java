package com.example.kafka.board;

import com.example.kafka.demo.DemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // Define custom query methods if needed
}
