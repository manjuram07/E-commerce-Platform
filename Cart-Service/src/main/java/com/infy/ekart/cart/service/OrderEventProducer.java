package com.infy.ekart.cart.service;

import com.infy.ekart.cart.dto.OrderCancelledEvent;
import com.infy.ekart.cart.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * Publishes Order domain events to Kafka.
 *
 * Key design decisions:
 * - Partition key = orderId.toString() → all events for one order land in
 *   the same partition, preserving ordering for downstream consumers.
 * - Custom headers carry correlationId and source service for tracing.
 * - Returns CompletableFuture so callers can choose fire-and-forget or
 *   awaited delivery (callers in OrderService use non-blocking).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.order-created}")
    private String orderCreatedTopic;

    @Value("${kafka.topics.order-cancelled}")
    private String orderCancelledTopic;

    // ── Public API ────────────────────────────────────────────────────────────

    public CompletableFuture<SendResult<String, Object>> publishOrderCreated(OrderCreatedEvent event) {
        log.info("[Producer] Publishing order.created | orderId={} correlationId={}",
                event.getOrderId(), event.getCorrelationId());

        var record = buildRecord(orderCreatedTopic, event.getOrderId().toString(), event,
                event.getCorrelationId().toString());

        return kafkaTemplate.send(record)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Producer] Failed to publish order.created | orderId={} error={}",
                                event.getOrderId(), ex.getMessage(), ex);
                    } else {
                        log.debug("[Producer] order.created acked | orderId={} partition={} offset={}",
                                event.getOrderId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    public CompletableFuture<SendResult<String, Object>> publishOrderCancelled(OrderCancelledEvent event) {
        log.info("[Producer] Publishing order.cancelled | orderId={} reason={} correlationId={}",
                event.getOrderId(), event.getReason(), event.getCorrelationId());

        var record = buildRecord(orderCancelledTopic, event.getOrderId().toString(), event,
                event.getCorrelationId().toString());

        return kafkaTemplate.send(record)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Producer] Failed to publish order.cancelled | orderId={} error={}",
                                event.getOrderId(), ex.getMessage(), ex);
                    } else {
                        log.debug("[Producer] order.cancelled acked | orderId={} partition={} offset={}",
                                event.getOrderId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private ProducerRecord<String, Object> buildRecord(String topic, String key, Object payload, String correlationId) {

        var record = new ProducerRecord<String, Object>(topic, key, payload);
        record.headers()
                .add(new RecordHeader("X-Correlation-Id",
                        correlationId.getBytes(StandardCharsets.UTF_8)))
                .add(new RecordHeader("X-Source-Service",
                        "order-service".getBytes(StandardCharsets.UTF_8)));
        return record;
    }
}
