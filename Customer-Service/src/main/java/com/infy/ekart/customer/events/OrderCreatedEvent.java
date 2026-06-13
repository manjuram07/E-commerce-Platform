package com.infy.ekart.customer.events;

import com.infy.ekart.customer.entity.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

// OrderCreatedEvent.java
public record OrderCreatedEvent(
        String orderId,
        String customerId,
        List<Order> items,
        BigDecimal totalAmount,
        Instant occurredAt
) {}