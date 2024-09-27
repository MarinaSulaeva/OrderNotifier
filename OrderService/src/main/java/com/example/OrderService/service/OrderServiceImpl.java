package com.example.OrderService.service;

import com.example.OrderService.dto.OrderDto;
import com.example.core.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());




    @Override
    public String createOrder(OrderDto orderDto) throws ExecutionException, InterruptedException {
        OrderEvent orderEvent = new OrderEvent(orderDto.getOrder(), orderDto.getQuantity());
        String topicId = UUID.randomUUID().toString();
        SendResult<String, OrderEvent> result = kafkaTemplate.send("order-topic", topicId, orderEvent).get();
        logger.error("Topic: {}", result.getRecordMetadata().topic());
        logger.info("Partition: {}", result.getRecordMetadata().partition());
        logger.info("Offset: {}", result.getRecordMetadata().offset());
        logger.info("Return: {}", topicId);
        return topicId;
    }
}
