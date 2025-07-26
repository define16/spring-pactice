package com.example.kafka.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PushMessageDto {
    private Long id;
    private String headline;
    private LocalDateTime savedAt;
}
