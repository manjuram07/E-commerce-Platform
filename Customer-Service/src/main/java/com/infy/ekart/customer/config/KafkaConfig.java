package com.infy.ekart.customer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    // ── Topic declarations ─────────────────────────────────────────────────
    @Bean
    public NewTopic orderCreatedTopic() {
        return TopicBuilder.name("order.created")
                .partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic orderCancelledTopic() {
        return TopicBuilder.name("order.cancelled")
                .partitions(3).replicas(1).build();
    }

    // ── Dead Letter Topics (auto-created by Spring when DLT handler present) ─
    @Bean
    public NewTopic paymentCompletedDlt() {
        return TopicBuilder.name("payment.completed.DLT")
                .partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic inventoryFailedDlt() {
        return TopicBuilder.name("inventory.failed.DLT")
                .partitions(1).replicas(1).build();
    }

    // ── Consumer factory with retry + DLT ────────────────────────────────
    @Bean
    public ConsumerFactory<String, Object> consumerFactory(KafkaProperties props) {
        Map<String, Object> config = new HashMap<>(props.buildConsumerProperties(null));
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.order.events");
        return new DefaultKafkaConsumerFactory<>(config,
                new StringDeserializer(),
                new JsonDeserializer<>(Object.class, false));
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> template) {
        // 3 retries with exponential backoff (1s → 2s → 4s), then send to DLT
        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(3);
        backOff.setInitialInterval(1_000L);
        backOff.setMultiplier(2.0);

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(template,
                        (record, ex) -> new TopicPartition(
                                record.topic() + ".DLT", record.partition()));

        return new DefaultErrorHandler(recoverer, backOff);
    }
}
