package ru.epa.epabackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.BodyFilter;
import org.zalando.logbook.HeaderFilter;
import org.zalando.logbook.core.HeaderFilters;

import java.util.Set;

import static org.zalando.logbook.BodyFilter.merge;
import static org.zalando.logbook.core.BodyFilters.defaultValue;
import static org.zalando.logbook.json.JsonBodyFilters.replaceJsonStringProperty;

@Configuration
public class LogbookConfiguration {
    @Bean
    public BodyFilter bodyFilter() {
        Set<String> properties = Set.of(
                "password",
                "token");

        return merge(
                defaultValue(),
                replaceJsonStringProperty(properties, "{masked}"));
    }

    @Bean
    public HeaderFilter headerFilter() {
        Set<String> headers = Set.of(
                "Authorization");

        return HeaderFilters.replaceHeaders(headers, "{masked}");
    }
}
