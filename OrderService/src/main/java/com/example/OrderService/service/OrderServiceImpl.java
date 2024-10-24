package com.example.OrderService.service;

import com.example.OrderService.dto.OrderDto;
import com.example.core.OrderEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{
    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Override
    public String createOrder(OrderDto orderDto) throws ExecutionException, InterruptedException {
        OrderEvent orderEvent = new OrderEvent(orderDto.getProduct(), orderDto.getQuantity());
        String topicId = UUID.randomUUID().toString();
        SendResult<String, OrderEvent> result = kafkaTemplate.send("order-topic", topicId, orderEvent).get();
        log.info("Topic: {}", result.getRecordMetadata().topic());
        log.info("Partition: {}", result.getRecordMetadata().partition());
        log.info("Offset: {}", result.getRecordMetadata().offset());
        log.info("Return: {}", topicId);
        return topicId;
    }
}
