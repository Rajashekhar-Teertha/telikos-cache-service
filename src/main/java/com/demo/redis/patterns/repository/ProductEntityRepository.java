package com.demo.redis.patterns.repository;

import com.demo.redis.patterns.entity.BookingModelEntity;
import com.demo.redis.patterns.entity.ProductEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductEntityRepository extends ReactiveCrudRepository<BookingModelEntity, String> {
}
