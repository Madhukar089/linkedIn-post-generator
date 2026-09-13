package com.madhukar.lip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AIConfig {

    @Bean
    public RestClient aiRestClient() {
        return RestClient.create();
    }
}