package com.lpandzic.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpandzic.*;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.*;

@AllArgsConstructor
@Configuration
public class SerdeConfiguration {

    private final ObjectMapper objectMapper;

    @Bean
    public JsonSerde<Foo> fooSerde() {
        return jsonSerde(Foo.class);
    }

    @Bean
    public JsonSerde<Bar> barSerde() {
        return jsonSerde(Bar.class);
    }

    @Bean
    public JsonSerde<FooBar> fooBarSerde() {
        return jsonSerde(FooBar.class);
    }

    @Bean
    public Serde<String> stringSerde() {
        return Serdes.String();
    }

    private <T> JsonSerde<T> jsonSerde(Class<T> type) {
        var jsonSerializer = new JsonSerializer<T>(objectMapper);
        var jsonDeserializer = new JsonDeserializer<T>(type, objectMapper);
        jsonDeserializer.addTrustedPackages("*");
        return new JsonSerde<>(jsonSerializer, jsonDeserializer);
    }
}
