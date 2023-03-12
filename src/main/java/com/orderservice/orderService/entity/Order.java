package com.orderservice.orderService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name ="Order_Details")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="product_Id")
    private long productId;
    @Column(name="quantity")
    private long quantity;
    @Column(name="order_date")
    private Instant orderDate;
    @Column(name="order_status")
    private String orderStatus;
    @Column(name="amount")
    private long amount;

}
