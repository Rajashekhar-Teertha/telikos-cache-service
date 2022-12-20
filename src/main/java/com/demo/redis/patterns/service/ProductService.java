package com.demo.redis.patterns.service;

import com.demo.redis.patterns.entity.BookingModelEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.maersk.telikos.model.Booking;
import reactor.core.publisher.Mono;

public interface ProductService {



    /*Mono<ProductDto> getProduct(Long id);

    Mono<Void> create(com.maersk.telikos.model.Booking booking);

    Mono<Void> saveWriteBehind(Booking booking);
    Mono<BookingModelEntity> getProductReadThrough(String id);

    Mono<Object> getThroughR2db(String id);
    Mono<Void> createCache(Object object, Object key);*/

    Mono<Object> getCache(String id);

    Mono<Object> putCache(String id, Object object);
}
