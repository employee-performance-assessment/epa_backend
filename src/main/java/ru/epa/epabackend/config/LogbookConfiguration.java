package ru.epa.epabackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Logbook;

import static org.zalando.logbook.core.HeaderFilters.*;
import static org.zalando.logbook.json.JsonPathBodyFilters.jsonPath;

@Configuration
public class LogbookConfiguration {
    @Bean
    public Logbook logbook() {
        Logbook logbook = Logbook.builder()
                .bodyFilter(jsonPath("$.password").replace("{masked}"))
                .bodyFilter(jsonPath("$.token").replace("{masked}"))
                .headerFilter(authorization())
                .build();
        return logbook;
    }
}
