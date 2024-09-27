package com.example.OrderStatusService.handler;

import com.example.OrderStatusService.entity.Order;
import com.example.OrderStatusService.entity.Status;
import com.example.OrderStatusService.exception.ClosedOrderException;
import com.example.OrderStatusService.repository.OrderRepository;
import com.example.core.OrderEvent;
import com.example.core.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@KafkaListener(topics = "order-event")
@RequiredArgsConstructor
public class OrderEventHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;
    private final OrderRepository orderRepository;

    public void Handle(OrderEvent orderEvent) throws ExecutionException, InterruptedException {
        Order order = orderRepository.findByProduct(orderEvent.getProduct()).orElse(new Order());
        if (order != null) {
            order.setQuantity(order.getQuantity());
            switch (order.getStatus()) {
                case CREATED:
                    order.setStatus(Status.IN_PROGRESS);
                    break;
                case FINISH:
                    throw new ClosedOrderException();
                case IN_PROGRESS:
                    order.setStatus(Status.FINISH);
                    break;
            }
        } else {
            order.setProduct(orderEvent.getProduct());
            order.setStatus(Status.CREATED);
            order.setQuantity(orderEvent.getQuantity());
        }
        order = orderRepository.save(order);
        String topicId = UUID.randomUUID().toString();
        OrderStatusEvent orderStatusEvent = new OrderStatusEvent(order.getStatus().toString(), OffsetDateTime.now());
        SendResult<String, OrderStatusEvent> result = kafkaTemplate.send("order-status-topic", topicId, orderStatusEvent).get();
        logger.error("Topic: {}", result.getRecordMetadata().topic());
        logger.info("Partition: {}", result.getRecordMetadata().partition());
        logger.info("Offset: {}", result.getRecordMetadata().offset());
        logger.info("Return: {}", topicId);
//        logger.info("Received message: {}", message);
//        logger.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}", key, partition, topic, timestamp);

    }
}
