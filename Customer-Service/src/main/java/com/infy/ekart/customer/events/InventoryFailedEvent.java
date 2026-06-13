package com.infy.ekart.customer.events;

import java.time.Instant;
import java.util.List;

// InventoryFailedEvent.java   (incoming — from Inventory Service)
public record InventoryFailedEvent(
        String orderId,
        String reason,
        List<String> failedSkus,
        Instant occurredAt
) {}