package com.infy.ekart.customer.kafka;

import com.infy.ekart.customer.events.InventoryFailedEvent;
import com.infy.ekart.customer.events.PaymentCompletedEvent;
import com.infy.ekart.customer.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderService orderService;

    // ── Consume: payment.completed ──────────────────────────────────────
    @KafkaListener(
            topics = "${kafka.topics.payment-completed}",
            groupId = "order-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePaymentCompleted(
            @Payload PaymentCompletedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Received payment.completed orderId={} offset={}", event.orderId(), offset);
        orderService.onPaymentCompleted(event);
    }

    // ── Consume: inventory.failed ───────────────────────────────────────
    @KafkaListener(
            topics = "${kafka.topics.inventory-failed}",
            groupId = "order-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleInventoryFailed(
            @Payload InventoryFailedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Received inventory.failed orderId={} skus={} offset={}",
                event.orderId(), event.failedSkus(), offset);
        orderService.onInventoryFailed(event);
    }

    // ── DLT handlers (observability, alerting, manual replay) ───────────
    @KafkaListener(topics = "payment.completed.DLT", groupId = "order-service-dlt-group")
    public void handlePaymentDlt(ConsumerRecord<String, Object> record) {
        log.error("DLT: payment.completed could not be processed. key={} value={}",
                record.key(), record.value());
        // TODO: alert via PagerDuty / persist to dead_letter table for manual replay
    }

    @KafkaListener(topics = "inventory.failed.DLT", groupId = "order-service-dlt-group")
    public void handleInventoryDlt(ConsumerRecord<String, Object> record) {
        log.error("DLT: inventory.failed could not be processed. key={} value={}",
                record.key(), record.value());
    }
}
