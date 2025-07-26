package com.example.kafka.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    // Jackson ObjectMapper 인스턴스 (스레드 안전)
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * DTO 객체를 JSON 문자열로 변환
     *
     * @param dto 변환할 객체
     * @return JSON 문자열
     */
    public static String toJson(Object dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("DTO → JSON 변환 실패", e);
        }
    }

    /**
     * JSON 문자열을 지정한 클래스 타입의 객체로 변환
     *
     * @param json  JSON 문자열
     * @param clazz 변환 대상 클래스
     * @param <T>   반환 타입
     * @return 역직렬화된 객체
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON → DTO 변환 실패", e);
        }
    }
}
