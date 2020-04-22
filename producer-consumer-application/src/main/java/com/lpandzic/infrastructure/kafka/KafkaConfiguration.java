package com.lpandzic.infrastructure.kafka;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.util.StringUtils;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@EnableKafkaStreams
@Configuration
public class KafkaConfiguration {

    @Bean(KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration streamsConfig(KafkaProperties kafkaProperties) {
        Map<String, Object> config = new HashMap<>();

        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-9603-application-id");
        config.put(StreamsConfig.consumerPrefix(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG),
                   kafkaProperties.getConsumer().getAutoOffsetReset());

        config.put(StreamsConfig.consumerPrefix(ConsumerConfig.GROUP_ID_CONFIG),
                   kafkaProperties.getConsumer().getGroupId());

        config.put(StreamsConfig.RETRIES_CONFIG, 10_000);
        config.put(StreamsConfig.CLIENT_ID_CONFIG, "kafka-9603-client-id");
        config.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        config.put(StreamsConfig.ROCKSDB_CONFIG_SETTER_CLASS_CONFIG, FooBarRocksDBConfigSetter.class);

        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.producerPrefix(ProducerConfig.ACKS_CONFIG), "all");
        config.put(StreamsConfig.producerPrefix(ProducerConfig.COMPRESSION_TYPE_CONFIG), "lz4");
        config.put(StreamsConfig.producerPrefix(ProducerConfig.LINGER_MS_CONFIG), 500);
        config.put(StreamsConfig.producerPrefix(ProducerConfig.BATCH_SIZE_CONFIG), 951_448);
        config.put(StreamsConfig.producerPrefix(ProducerConfig.RETRIES_CONFIG), 10_000);
        config.put(StreamsConfig.producerPrefix(ProducerConfig.BUFFER_MEMORY_CONFIG), 52428800); //50MB
        config.put(StreamsConfig.producerPrefix(ProducerConfig.SEND_BUFFER_CONFIG), 1000 * 65536); //62.5MB

        config.put(StreamsConfig.consumerPrefix(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG), 2000);
        config.put(StreamsConfig.consumerPrefix(ConsumerConfig.RECEIVE_BUFFER_CONFIG), 1000 * 65536); //62.5MB
        config.put(StreamsConfig.consumerPrefix(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG),
                   10 * 1048576); // 10MB
        config.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(StreamsConfig.STATE_DIR_CONFIG, Paths.get("state").toAbsolutePath().toString());
        config.put(StreamsConfig.NUM_STANDBY_REPLICAS_CONFIG, 1);
        config.put(StreamsConfig.DEFAULT_PRODUCTION_EXCEPTION_HANDLER_CLASS_CONFIG,
                   CustomProductionExceptionHandler.class);
        config.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                   CustomDeserializationExceptionHandler.class);

        return new KafkaStreamsConfiguration(config);
    }

    @Bean
    public KafkaAdmin admin(KafkaProperties kafkaProperties) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic fooTopic() {
        return new NewTopic("foo", 48, (short) 6);
    }

    @Bean
    public NewTopic barTopic() {
        return new NewTopic("bar", 48, (short) 6);
    }
}
