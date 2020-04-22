package com.lpandzic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.concurrent.atomic.AtomicLong;

@AllArgsConstructor
@Service
public class DummyKafkaWritter {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final RecordsCounter recordsCounter;
    private final AtomicLong keyCounter = new AtomicLong(0L);
    private final Clock clock;

    @Scheduled(fixedRate = 20)
    public void send() throws JsonProcessingException {
        var key = Long.toString(keyCounter.incrementAndGet());
        var foo = new Foo("foo " + key, clock.instant());
        var bar = new Bar("bar " + key, clock.instant());
        kafkaTemplate.send(FooBarMerger.FOO_TOPIC_NAME, key,
                           objectMapper.writeValueAsString(foo));
        kafkaTemplate.send(FooBarMerger.BAR_TOPIC_NAME, key,
                           objectMapper.writeValueAsString(bar));
        recordsCounter.recordWritten();
    }
}
