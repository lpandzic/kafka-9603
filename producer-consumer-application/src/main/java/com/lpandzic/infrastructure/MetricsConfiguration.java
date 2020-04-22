package com.lpandzic.infrastructure;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class MetricsConfiguration {

    @Bean
    public OpenFilesGauge openFilesGauge(MeterRegistry registry) {
        var openFilesGauge = new OpenFilesGauge(ProcessHandle.current().pid());
        return registry.gauge("open_files_count", openFilesGauge, OpenFilesGauge::measure);
    }
}
