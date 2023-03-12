package com.orderservice.orderService.external.clilent;

import com.orderservice.orderService.exception.CustomException;
import com.orderservice.orderService.external.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name="external",fallbackMethod = "fallback")
@FeignClient(name="PAYMENT-SERVICE/payment")
public interface PaymentService {

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest request);

    default void fallback(Exception e){
        throw new CustomException("paymentService not available","Unavailable",500);
    }
}
