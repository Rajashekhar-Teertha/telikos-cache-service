package com.demo.redis.patterns.repository;

import com.demo.redis.patterns.entity.BookingModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<BookingModel, String> {
}
