package com.madhukar.lip.service;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private static final Duration WINDOW = Duration.ofMinutes(10);

    private final Map<String, Instant> lastRequestTime =
            new ConcurrentHashMap<>();

    public boolean isAllowed(String clientIp) {

        Instant now = Instant.now();

        Instant previousRequest =
                lastRequestTime.get(clientIp);

        if (previousRequest == null ||
                Duration.between(previousRequest, now).compareTo(WINDOW) >= 0) {

            lastRequestTime.put(clientIp, now);

            return true;
        }

        return false;
    }
}