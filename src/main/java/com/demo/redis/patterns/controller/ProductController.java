package com.demo.redis.patterns.controller;

import com.demo.redis.patterns.entity.BookingModelEntity;
import com.demo.redis.patterns.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.maersk.telikos.model.Booking;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

   /* @PostMapping("/saveWriteThrough")
    public Mono<Void> save(@RequestBody com.maersk.telikos.model.Booking booking) {
        return productService.create(booking);
    }


    @PostMapping("/saveWriteThrough1/{key}")
    public Mono<Void> save(@RequestBody Object object, @PathVariable Object key) {
        return productService.createCache(object, key);
    }

    @PostMapping("/saveWriteBehind")
    public Mono<Void> saveWriteBehind(@RequestBody Booking booking) {
        return productService.saveWriteBehind(booking);
    }

    @GetMapping("readthrough/{id}")
    public Mono<BookingModelEntity> getProductReadthrough(@PathVariable String id){
        return this.productService.getProductReadThrough(id);
    }


    @GetMapping("readthroughR2/{id}")
    public Mono<Object> getReadthroughR2(@PathVariable String id){
        return this.productService.getThroughR2db(id);
    }
*/

    /**
     * @param id
     * @return object
     */
    @GetMapping("/cacheGet/{id}")
    public Mono<Object> getCache(@PathVariable String id){
        log.info("processing get cache method {}", id);
        return productService.getCache(id);
    }

    /**
     * @param id
     * @return object
     */
    @PostMapping("/cachePut/{id}")
    public Mono<Object> putCache(@RequestBody Object object, @PathVariable("id") String id){
        log.info("processing put cache method {}",object);
        return this.productService.putCache(id, object);
    }
}
