package com.infy.ekart.customer.events;

import java.math.BigDecimal;
import java.time.Instant;

// PaymentCompletedEvent.java  (incoming — from Payment Service)
public record PaymentCompletedEvent(
        String paymentId,
        String orderId,
        BigDecimal amount,
        Instant occurredAt
) {}