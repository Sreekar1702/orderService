package com.orderservice.orderService.service;

import com.orderservice.orderService.model.OrderResponse;
import com.orderservice.orderService.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest request);

    OrderResponse getOrderById(long orderId);
}
