package com.orderservice.orderService.controller;

import com.orderservice.orderService.model.OrderResponse;
import com.orderservice.orderService.model.OrderRequest;
import com.orderservice.orderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placceOrder(@RequestBody OrderRequest request){
            long orderId = orderService.placeOrder(request);
            return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

    @GetMapping("/order/{id}")
    private ResponseEntity<OrderResponse> getOrderDetails(@PathVariable("id") long orderId){
        log.info("invoking product service to fetch productId");
        OrderResponse orderResponse = orderService.getOrderById(orderId);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }

}
