package com.lpandzic;

import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

@AllArgsConstructor
@Component
public class FooBarMerger {

    static final String FOO_TOPIC_NAME = "foo";
    static final String BAR_TOPIC_NAME = "bar";
    private final Serde<Foo> fooSerde;
    private final Serde<String> stringSerde;
    private final Serde<Bar> barSerde;
    private final StreamsBuilder builder;
    private final RecordsCounter recordsCounter;

    @PostConstruct
    public void start() {
        var foos = builder.stream(FOO_TOPIC_NAME,
                                  Consumed.with(stringSerde, fooSerde,
                                                fooTimestampExtractor(), null));

        var bars = builder.stream(BAR_TOPIC_NAME,
                                  Consumed.with(stringSerde, barSerde, barTimestampExtractor(), null));

        var joinWindows = JoinWindows.of(Duration.of(1, MINUTES))
                                                .grace(Duration.of(7, DAYS));

        foos.join(bars, this::join, joinWindows,
                  Joined.with(stringSerde, fooSerde, barSerde))
            .foreach((key, value) -> recordsCounter.recordMerged());
    }

    private FooBar join(Foo foo, Bar bar) {
        return new FooBar(foo.getFoo(), bar.getBar());
    }

    private TimestampExtractor fooTimestampExtractor() {
        return (record, oldTimestamp) -> ((Foo) record.value()).getCreatedAt().toEpochMilli();
    }

    private TimestampExtractor barTimestampExtractor() {
        return (record, oldTimestamp) -> ((Bar) record.value()).getCreatedAt().toEpochMilli();
    }
}
