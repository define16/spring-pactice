package com.example.kafka.config;


import com.example.kafka.board.dto.PushMessage;
import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

@Configuration
public class GraphqlConfig {

    @Bean
    public Sinks.Many<PushMessage> pushMessageManySink() {
        //  autoCancel=false 구독자가 모두 취소되었을 때 Sink가 새로운 구독자를 받지 않도록 하는 동작을 제어
        return Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                // DateTime 스칼라 (java.time.OffsetDateTime ↔ ISO-8601 문자열)
                .scalar(ExtendedScalars.DateTime)
                // Long 스칼라 (java.lang.Long ↔ GraphQL Long)
                .scalar(ExtendedScalars.GraphQLLong);
    }
}
