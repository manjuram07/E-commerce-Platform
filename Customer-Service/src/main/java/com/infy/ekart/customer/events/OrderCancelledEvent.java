package com.infy.ekart.customer.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

// OrderCancelledEvent.java
public record OrderCancelledEvent(
        String orderId,
        String reason,       // e.g. "INVENTORY_FAILED"
        Instant occurredAt
) {}
