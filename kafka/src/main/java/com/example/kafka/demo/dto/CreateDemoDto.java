package com.example.kafka.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateDemoDto {
    private String headline;
    private String content;
}
