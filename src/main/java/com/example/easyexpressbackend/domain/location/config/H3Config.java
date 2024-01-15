package com.example.easyexpressbackend.domain.location.config;

import com.uber.h3core.H3Core;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H3Config {

    @SneakyThrows
    @Bean
    public H3Core h3Core() {
        return H3Core.newInstance();
    }
}
