package com.demo.redis.patterns.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TTLConfig {

    @Value("${cache-ttls.booking}")
    private Long bookingDataTTL;

}
