package com.example.kafka.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoRepository extends JpaRepository<DemoEntity, Long> {
    // Define custom query methods if needed
}
