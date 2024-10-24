package com.example.OrderStatusService.handler;

import com.example.OrderStatusService.entity.Order;
import com.example.OrderStatusService.entity.Status;
import com.example.OrderStatusService.exception.ClosedOrderException;
import com.example.OrderStatusService.repository.OrderRepository;
import com.example.core.OrderEvent;
import com.example.core.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventHandler {

    @Autowired
    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "order-topic")
    public void Handle(OrderEvent orderEvent) throws ExecutionException, InterruptedException {
        Order order = orderRepository.findByProduct(orderEvent.getProduct()).orElse(new Order());
        if (!(order.getId() ==null)) {
            order.setQuantity(order.getQuantity());
            switch (order.getStatus()) {
                case CREATED -> order.setStatus(Status.IN_PROGRESS);
                case FINISH -> throw new ClosedOrderException();
                case IN_PROGRESS -> order.setStatus(Status.FINISH);
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
        log.info("Topic: {}", result.getRecordMetadata().topic());
        log.info("Partition: {}", result.getRecordMetadata().partition());
        log.info("Offset: {}", result.getRecordMetadata().offset());
        log.info("Return: {}", topicId);
    }
}
