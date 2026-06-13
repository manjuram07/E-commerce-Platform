package com.infy.ekart.customer.kafka;

import com.infy.ekart.customer.events.OrderCancelledEvent;
import com.infy.ekart.customer.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.order-created}")
    private String orderCreatedTopic;

    @Value("${kafka.topics.order-cancelled}")
    private String orderCancelledTopic;

    public void publishOrderCreated(OrderCreatedEvent event) {
        // Key = orderId → same order always lands on same partition (ordering guarantee)
        kafkaTemplate.send(orderCreatedTopic, event.orderId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish order.created for orderId={}", event.orderId(), ex);
                    } else {
                        log.info("Published order.created orderId={} offset={}",
                                event.orderId(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    public void publishOrderCancelled(OrderCancelledEvent event) {
        kafkaTemplate.send(orderCancelledTopic, event.orderId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish order.cancelled for orderId={}", event.orderId(), ex);
                    } else {
                        log.info("Published order.cancelled orderId={}", event.orderId());
                    }
                });
    }
}
