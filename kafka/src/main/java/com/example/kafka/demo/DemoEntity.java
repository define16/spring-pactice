package com.example.kafka.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DemoEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String headline;
    private String content;

    public DemoEntity(String headline, String content) {
        this.headline = headline;
        this.content = content;
    }
}
