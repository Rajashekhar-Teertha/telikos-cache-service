package com.demo.redis.patterns.config;

import com.demo.redis.patterns.entity.BookingModel;
import com.demo.redis.patterns.entity.BookingModelEntity;
import com.demo.redis.patterns.repository.ProductEntityRepository;
import com.demo.redis.patterns.repository.ProductRepository;
import com.demo.redis.patterns.util.EntityDtoUtil;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maersk.telikos.model.Booking;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import static org.springframework.data.r2dbc.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Configuration
public class AppConfig {

    private final String CACHE_NAME = "test-cache1";

    @Value("${model.booking}")
    private String modelType;

    @Value("${model.serviceplan}")
    private String modelType1;


    @Autowired
    RedisConfig redisConfig;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductEntityRepository productEntityRepository;

    @Autowired
    R2dbcEntityTemplate r2dbcEntityTemplate;


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
    public RMapCacheReactive<String, com.maersk.telikos.model.Booking> writeThroughRMapCache() {
        final RMapCacheReactive<String, Booking> bookingRMapCache = redissonReactiveClient().getMapCache(CACHE_NAME, StringCodec.INSTANCE,
                MapOptions.<String,Booking>defaults()
                .writer(getWriteThroughMapWriter())
                .writeMode(MapOptions.WriteMode.WRITE_THROUGH));

        return bookingRMapCache;
    }


    private MapWriter<String, com.maersk.telikos.model.Booking> getWriteThroughMapWriter() {
        return new MapWriter<String, Booking>() {

            @Override
            public void write(final Map<String, Booking> map) {

                map.forEach((k, v) -> {
                    BookingModel booking = EntityDtoUtil.toEntity(v);
                    productRepository.save(booking.setAsNew()).subscribe();
                });
            }
            @Override
            public void delete(Collection<String> keys) {
                keys.stream().forEach(e -> {

                    try {
                        productRepository.delete(productRepository.findById(e).toFuture().get());
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        };
    }

    @Bean
    @DependsOn("redissonReactiveClient")
    public RMapCacheReactive<String, Booking> writeBehindRMapCache() {
        final RMapCacheReactive<String, Booking> employeeRMapCache = redissonReactiveClient().getMapCache(CACHE_NAME, StringCodec.INSTANCE, MapOptions.<String, Booking>defaults()
                .writer(getMapWriterForWriteBehind())
                .writeBehindDelay(60000)
                .writeMode(MapOptions.WriteMode.WRITE_BEHIND));

        return employeeRMapCache;
    }

    private MapWriter<String, Booking> getMapWriterForWriteBehind() {
        return new MapWriter<String, Booking>() {

            @Override
            public void write(final Map<String, Booking> map) {

                map.forEach((k, v) -> {
                    System.out.println("in write behind getWritermethod===" + v.getBookingId());
                    BookingModel product = EntityDtoUtil.toEntity(v);
                    productRepository.save(product.setAsNew()).subscribe();
                    System.out.println("in write behind aftersave===");

                });
            }

            @Override
            public void delete(Collection<String> keys) {
                keys.stream().forEach(e -> {
                    try {
                        productRepository.delete(productRepository.findById(e).toFuture().get());
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        };
    }


    @Bean
    @DependsOn("redissonReactiveClient")
    public RMapCacheReactive<String, BookingModelEntity> readThroughRMapCacheReader() {
        System.out.println("in employeeRMapCacheReader");
        final RMapCacheReactive<String, BookingModelEntity> employeeRMapCache = redissonReactiveClient().getMapCache(CACHE_NAME, StringCodec.INSTANCE, MapOptions.<String, BookingModelEntity>defaults()
                .loader(readeThroughMapLoader));
        return employeeRMapCache;
    }

    MapLoader<String, BookingModelEntity> readeThroughMapLoader = new MapLoader<String, BookingModelEntity>() {

        @Override
        public BookingModelEntity load(String id) {
            try {
                System.out.println("in maploader");
                return productEntityRepository.findById(id).toFuture().get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Iterable<String> loadAllKeys() {
            return null;
        }


    };

    @Bean
    @DependsOn("redissonReactiveClient")
    public RMapCacheReactive<String, Object> readThroughRMapCacheReaderR2DB() {
        final RMapCacheReactive<String, Object> employeeRMapCache = redissonReactiveClient().getMapCache(CACHE_NAME, StringCodec.INSTANCE, MapOptions.<String, Object>defaults()
                .loader(readeThroughMapLoader1));
        return employeeRMapCache;
    }

    MapLoader<String, Object> readeThroughMapLoader1 = new MapLoader<String, Object>() {

        @Override
        public Object load(String id) {
            try {
                Class claz = Class.forName(modelType).newInstance().getClass();
                return r2dbcEntityTemplate.selectOne(query(where("bookingId").is(id)), claz).toFuture().get();

            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | InterruptedException |
                     ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Iterable<String> loadAllKeys() {
            return null;
        }
    };

    @Bean
    @DependsOn("redissonReactiveClient")
    public RMapCacheReactive<String, Object> writeThroughRMapCache1() {
        final RMapCacheReactive<String, Object> bookingRMapCache = redissonReactiveClient().getMapCache(CACHE_NAME, StringCodec.INSTANCE,
                MapOptions.<String,Object>defaults()
                        .writer(getWriteThroughMapWriter1())
                        .writeMode(MapOptions.WriteMode.WRITE_THROUGH));

        return bookingRMapCache;
    }

    private MapWriter<String, Object> getWriteThroughMapWriter1() {
        return new MapWriter<String, Object>() {

            @Override
            public void write(final Map<String, Object> map) {

                map.forEach((k, v) -> {
                     r2dbcEntityTemplate.insert(getModel(v)).subscribe();
                });
            }
            @Override
            public void delete(Collection<String> keys) {
                keys.stream().forEach(e -> {
                    r2dbcEntityTemplate.delete(e);
                   /* try {
                        productRepository.delete(productRepository.findById(e).toFuture().get());
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }*/
                });
            }
        };
    }

    public Object getModel(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Class<?> claz = Class.forName(modelType).newInstance().getClass();
            return mapper.convertValue(object, claz);

        }  catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    @DependsOn("redissonReactiveClient")
    public RMapCacheReactive<String, Object> rMapCacheClient() {
        final RMapCacheReactive<String, Object> bookingRMapCache = redissonReactiveClient().getMapCache(CACHE_NAME, JsonJacksonCodec.INSTANCE,
                MapOptions.<String,Object>defaults());

        return bookingRMapCache;
    }



}
