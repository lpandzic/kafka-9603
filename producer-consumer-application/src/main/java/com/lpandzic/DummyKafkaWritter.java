package com.lpandzic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;

@AllArgsConstructor
@Service
public class DummyKafkaWritter {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final RecordsCounter recordsCounter;
    private final Clock clock;

    @Scheduled(fixedRate = 100)
    public void send() throws JsonProcessingException {
        var timestamp = clock.instant();
        var key = timestamp.toString();
        var foo = new Foo("foo " + key, timestamp);
        var bar = new Bar("bar " + key, timestamp);
        kafkaTemplate.send(FooBarMerger.FOO_TOPIC_NAME, key,
                           objectMapper.writeValueAsString(foo));
        kafkaTemplate.send(FooBarMerger.BAR_TOPIC_NAME, key,
                           objectMapper.writeValueAsString(bar));
        recordsCounter.recordWritten();
    }
}
