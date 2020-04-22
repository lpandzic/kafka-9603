package com.lpandzic.infrastructure.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import javax.annotation.PostConstruct;

@Slf4j
@AllArgsConstructor
@Configuration
public class StreamBuilderFactoryBeanCustomizer {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @PostConstruct
    public void init() {
        streamsBuilderFactoryBean.setUncaughtExceptionHandler(
                (thread, throwable) -> log.error("Uncaught exception in stream {}", thread, throwable));
    }
}
