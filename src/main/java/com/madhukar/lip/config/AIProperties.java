package com.madhukar.lip.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai")
public record AIProperties(
        String provider,
        String apiKey,
        String model
) {
}