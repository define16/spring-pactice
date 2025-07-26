package com.example.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;

public class GraphqlSubscribeTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    // 블록용 래치: 종료 전까지 main 스레드가 살아있도록 함
    private static final CountDownLatch LATCH = new CountDownLatch(1);

    @Test
    void websocketTest() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        client.newWebSocketBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .subprotocols("graphql-transport-ws")
                .buildAsync(URI.create("ws://localhost:7001/subscribe"), new SubscriptionListener())
                .join();

        // 프로그램이 종료되지 않도록 대기
        LATCH.await();
    }

    private static class SubscriptionListener implements WebSocket.Listener {
        private boolean ackReceived = false;

        @Override
        public void onOpen(WebSocket webSocket) {
            System.out.println("▶ WebSocket OPEN");
            // 1) connection_init
            Map<String, Object> initMsg = Map.of(
                    "type", "connection_init",
                    "payload", Collections.emptyMap()
            );
            sendJson(webSocket, initMsg);
            WebSocket.Listener.super.onOpen(webSocket);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            String msg = data.toString();
            System.out.println("◀ RECEIVED: " + msg);

            // 2) connection_ack 받으면 구독 시작
            if (!ackReceived && msg.contains("\"type\":\"connection_ack\"")) {
                ackReceived = true;

                String query = "subscription BoardSubscription($boardId: Long!) { " +
                        "boardSubscription(boardId: $boardId) { id headline savedAt } }";

                Map<String, Object> payload = new HashMap<>();
                payload.put("query", query);
                payload.put("variables", Map.of("boardId", 502));

                Map<String, Object> startMsg = Map.of(
                        "id", "1",
                        "type", "subscribe",
                        "payload", payload
                );
                sendJson(webSocket, startMsg);
            }

            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            System.err.println("⚠ WebSocket error: " + error.getMessage());
            WebSocket.Listener.super.onError(webSocket, error);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            System.out.printf("✖ WebSocket closed: code=%d, reason=%s%n", statusCode, reason);
            LATCH.countDown();
            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
        }

        private void sendJson(WebSocket ws, Map<String, Object> msg) {
            try {
                String json = mapper.writeValueAsString(msg);
                ws.sendText(json, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
