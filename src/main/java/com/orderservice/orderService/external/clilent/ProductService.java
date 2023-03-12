package com.orderservice.orderService.external.clilent;

import com.orderservice.orderService.exception.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name="external",fallbackMethod = "fallback")
@FeignClient(name ="PRODUCT-SERVICES/product")
public interface ProductService {

    @PutMapping("/reduceQuantity/{id}")
    ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId,
                                               @RequestParam long quantity);

    default void fallback(Exception e){
        throw new CustomException("productService not available","Unavailable",500);
    }
}
