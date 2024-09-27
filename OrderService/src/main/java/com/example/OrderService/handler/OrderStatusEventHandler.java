package com.example.OrderService.handler;

import com.example.core.OrderStatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "order-status-event")
public class OrderStatusEventHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void Handle(OrderStatusEvent orderStatusEvent) {
        String message = "статус заказа: " + orderStatusEvent.getStatus() + ", дата " + orderStatusEvent.getOffsetDateTime();
        logger.info("Received message: {}", message);
//        logger.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}", key, partition, topic, timestamp);

    }
}
