package com.orderservice.orderService.service;

import brave.messaging.ProducerResponse;
import com.orderservice.orderService.entity.Order;
import com.orderservice.orderService.external.response.PaymentResponse;
import com.orderservice.orderService.model.OrderResponse;
import com.orderservice.orderService.exception.CustomException;
import com.orderservice.orderService.external.clilent.PaymentService;
import com.orderservice.orderService.external.clilent.ProductService;
import com.orderservice.orderService.external.request.PaymentRequest;
import com.orderservice.orderService.model.OrderRequest;
import com.orderservice.orderService.model.ProductResponse;
import com.orderservice.orderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceimpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("place order");
        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order = orderRepository.save(order);
        log.info("calling paymentService to conplete payment");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();
        String orderStatus = null;
        try{
            paymentService.doPayment(paymentRequest);
            orderStatus="PLACED";
        } catch(Exception e) {
            log.error("Exception while payment");
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        return order.getProductId();
    }

    @Override
    public OrderResponse getOrderById(long orderId) {
        log.info("invoking product service to fetch productId");
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()->new CustomException("OrderIdNotFound","Order_Not_Found",404));
        log.info("invoking product service to fetch productId");
        ProductResponse producerResponse = restTemplate.getForObject(
                "http://PRODUCT-SERVICES/product/"+order.getProductId(),ProductResponse.class
        );

        log.info("Getting payment response");
        PaymentResponse paymentResponse = restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/"+order.getId(),PaymentResponse.class
        );

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .paymentStatus(paymentResponse.getStatus())
                .build();

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails
                .builder()
                .productName(producerResponse.getProductName())
                .productId(producerResponse.getProductId())
                .build();
        OrderResponse orderResponse = OrderResponse.builder()
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
        return orderResponse;
    }
}