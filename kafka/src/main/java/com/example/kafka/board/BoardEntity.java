package com.example.kafka.board;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
public class BoardEntity {
    // Demo로 POST가 들어오고 kafak로 메시지를 발생시키고 Board에서 받아서 저장하는 로직
    @Id
    @GeneratedValue
    private Long id;

    private Long demoId;
    private String headline;
    private LocalDateTime savedAt;

    public BoardEntity(Long demoId, String headline) {
        this.demoId = demoId;
        this.headline = headline;
        this.savedAt = LocalDateTime.now();
    }
}
