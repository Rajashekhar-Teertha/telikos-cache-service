package com.demo.redis.patterns.service.impl;

import com.demo.redis.patterns.exception.CacheProcessingException;
import com.demo.redis.patterns.config.TTLConfig;
import com.demo.redis.patterns.entity.BookingModelEntity;
import com.demo.redis.patterns.service.ProductService;
import com.maersk.telikos.model.Booking;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    TTLConfig ttlConfig;
    @Autowired
    RMapCacheReactive<String, Object> rMapCacheClient;
    @Autowired
    private RedissonReactiveClient redissonClient;
    @Autowired
    private RMapCacheReactive<String, Booking> writeThroughRMapCache;
    @Autowired
    private RMapCacheReactive<String, Object> writeThroughRMapCache1;
    @Autowired
    private RMapCacheReactive<String, Booking> writeBehindRMapCache;
    @Autowired
    private RMapCacheReactive<String, BookingModelEntity> readThroughRMapCacheReader;
    @Autowired
    private RMapCacheReactive<String, Object> readThroughRMapCacheReaderR2DB;
  /*@Override
  public Mono<Void> create(com.maersk.telikos.model.Booking booking) {

    return this.writeThroughRMapCache.put(booking.getBookingId(), booking,ttlConfig.getBookingDataTTL(), TimeUnit.MINUTES).then();
//     return this.writeThroughRMapCache.put(booking.getBookingId(), booking).then();
  }

  @Override
  public Mono<Void> saveWriteBehind(Booking booking) {
    return this.writeBehindRMapCache.put(booking.getBookingId(), booking).then();
  }

  @Override
  public Mono<BookingModelEntity> getProductReadThrough(String id) {
    System.out.println("in getProductReadThrough");
    return this.readThroughRMapCacheReader.get(id);
  }

  *//**
     * @param id
     * @return
     *//*
  @Override
  public Mono<Object> getThroughR2db(String id) {
    return this.readThroughRMapCacheReaderR2DB.get(id);
  }*/


    /**
     * @param id
     * @return
     */
    @Override
    public Mono<Object> getCache(String id) {
      try{
        log.info("reading from cache with value key {} ",id);
        return rMapCacheClient.get(id);
      }
      catch(Exception e){
        log.error("exception occurred while fetching data from cache {}", e.getMessage());
        throw new CacheProcessingException(e.getMessage());
      }

    }

    /**
     * @param id
     * @param object
     * @return
     */
    @Override
    public Mono<Object> putCache(String id, Object object) {

      try{
        log.info("writing to cache with value key {} , object {}",id,object);
        return rMapCacheClient.put(id, object);
        //    return rMapCacheClient.put(id,object, ttlConfig.getBookingDataTTL(), TimeUnit.SECONDS);
      }
      catch(Exception e){
        log.error("exception occurred while processing the cache {}", e.getMessage());
        throw new CacheProcessingException(e.getMessage());
      }

    }







    /*  *//**
     * @param object
     * @return
     *//*
  @Override
  public Mono<Void> createCache(Object object, Object key) {

    return this.writeThroughRMapCache1.put(getKey(object, key), object,ttlConfig.getBookingDataTTL(), TimeUnit.MINUTES).then();
  }

  private String getKey(Object object, Object key) {
    try {

      ObjectMapper mapper = new ObjectMapper();
      String str = mapper.writeValueAsString(object);
      JSONObject jsonObject = new JSONObject(str);
      return jsonObject.get(key.toString()).toString();

    } catch(JsonProcessingException e){
      throw new RuntimeException();
    }

  }*/

}
