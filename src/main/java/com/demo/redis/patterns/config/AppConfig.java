package com.demo.redis.patterns.config;

import org.redisson.Redisson;
import org.redisson.api.MapOptions;
import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    private final String CACHE_NAME = "test-cache1";

    @Bean("redissonReactiveClient")
    public RedissonReactiveClient redissonReactiveClient() {

        //  final Codec codec = new SerializationCodec();
        Config config = new Config();
        //    config.setCodec(codec);

        config.useSingleServer().setAddress
                ("redis://127.0.0.1:6379");

//        String redisProtocol = Boolean.parseBoolean("true") ? "rediss" : "redis";
//        config.useSingleServer().
//        setSslProtocols(new String[] { "TLSv1.2" }).
//        setSslEnableEndpointIdentification(true).
//        setSslEnableEndpointIdentification(Boolean.valueOf("true")).
//        setPassword("Vo3jLiRNk5RQgjuPas1S11CJfITx34SfFAzCaIdsdJ8=").
//       setAddress(redisProtocol + "://redis-telikos-poc.redis.cache.windows.net:6380");


        RedissonClient redisson = Redisson.create(config);
        RedissonReactiveClient redissonReactive = redisson.reactive();

        return redissonReactive;
    }


    @Bean
    @DependsOn("redissonReactiveClient")
    public RMapCacheReactive<String, Object> rMapCacheClient() {
        final RMapCacheReactive<String, Object> bookingRMapCache = redissonReactiveClient().getMapCache(CACHE_NAME, JsonJacksonCodec.INSTANCE,
                MapOptions.<String,Object>defaults());
//        bookingRMapCache.expire(10, TimeUnit.SECONDS);

        return bookingRMapCache;
    }

    /*@Bean
    public RMapCacheReactive<Object, Object> bookingCache() {
        RedissonClient redisson1 = Redisson.create();
        RedissonReactiveClient redisson = redisson1.reactive();
        RMapCacheReactive<Object, Object> map = redisson.getMapCache("booking-map" );
        map.expire(10, TimeUnit.SECONDS);
        return map;
    }*/

    /*public Object getModel(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Class<?> claz = Class.forName(modelType).newInstance().getClass();
            return mapper.convertValue(object, claz);

        }  catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }*/

}
