package com.demo.redis.patterns.service;

import com.demo.redis.patterns.entity.BookingModelEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.maersk.telikos.model.Booking;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<Object> getCache(String id);

    Mono<Object> putCache(String id, Object object);
}
