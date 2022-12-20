package com.demo.redis.patterns.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RedisConfig {

    @Value("${redis-config.setmax-size}")
    private int setMaxSize;

    @Value("${redis-config.eviction-type}")
    private String evictionModeType;

}
