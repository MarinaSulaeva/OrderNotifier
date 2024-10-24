package com.example.OrderService.controller;

import com.example.OrderService.dto.OrderDto;
import com.example.OrderService.exception.SendException;
import com.example.OrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto) {
        String topicId = null;
        try {
            topicId = orderService.createOrder(orderDto);
        } catch (Exception e) {
            throw new SendException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(topicId);
    }
}
