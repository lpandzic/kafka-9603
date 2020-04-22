package com.lpandzic.infrastructure.kafka;

import org.apache.kafka.streams.state.RocksDBConfigSetter;
import org.rocksdb.InfoLogLevel;
import org.rocksdb.Options;

import java.util.Map;

public class FooBarRocksDBConfigSetter implements RocksDBConfigSetter {

    @Override
    public void setConfig(String storeName, Options options, Map<String, Object> configs) {
        options.setAllowMmapReads(true);
        options.setAllowMmapWrites(true);
        options.setIncreaseParallelism(3);
        options.setInfoLogLevel(InfoLogLevel.WARN_LEVEL);
        options.setKeepLogFileNum(1);
        options.setMaxLogFileSize(1024 * 1024L);
    }

    @Override
    public void close(String storeName, Options options) {

    }
}
