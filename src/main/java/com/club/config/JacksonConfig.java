package com.club.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder
                .dateFormat(new CustomDateFormat())
                .build();
    }

    public static class CustomDateFormat extends StdDateFormat {
        @Override
        public String toString() {
            return "yyyy-MM-dd HH:mm:ss";
        }
    }
}
