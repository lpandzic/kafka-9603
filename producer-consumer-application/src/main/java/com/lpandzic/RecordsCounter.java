package com.lpandzic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
@Component
public class RecordsCounter {

    private final LongAdder numberOfWrittenRecords = new LongAdder();
    private final LongAdder numberOfMergedRecords = new LongAdder();

    public void recordWritten() {
        numberOfWrittenRecords.increment();
    }

    public void recordMerged() {
        numberOfMergedRecords.increment();
    }

    @Scheduled(fixedRate = 10000)
    public void log() {
        log.info("Records written: {}, merged {}. ", numberOfWrittenRecords.longValue(),
                 numberOfMergedRecords.longValue());
    }
}
