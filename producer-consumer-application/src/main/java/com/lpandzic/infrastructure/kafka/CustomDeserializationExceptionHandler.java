package com.lpandzic.infrastructure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.errors.DeserializationExceptionHandler;
import org.apache.kafka.streams.processor.ProcessorContext;

import java.util.Map;

@Slf4j
public class CustomDeserializationExceptionHandler implements DeserializationExceptionHandler {
    @Override
    public DeserializationHandlerResponse handle(ProcessorContext processorContext,
                                                 ConsumerRecord<byte[], byte[]> record,
                                                 Exception exception) {
        log.error(
                "Error on deserialization. Kafka message marked as processed although it failed. Message: [{}], destination topic: [{}]",
                new String(record.value()), record.topic(), exception);
        return DeserializationHandlerResponse.CONTINUE;
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
