package com.example.OrderService.handler;

import com.example.core.OrderStatusEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderStatusEventHandler {

    @KafkaListener(topics = "order-status-topic")
    public void listenStatus(Message<OrderStatusEvent> message) {
        String key = message.getHeaders().get("kafka_receivedMessageKey", String.class);
        Integer partition = message.getHeaders().get("kafka_receivedPartitionId", Integer.class);
        String topic = message.getHeaders().get("kafka_receivedTopic", String.class);
        String timestamp = message.getHeaders().get("timestamp", String.class);

        log.info("Received message: {}", message);
        log.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}", key, partition, topic, timestamp);
    }
}
