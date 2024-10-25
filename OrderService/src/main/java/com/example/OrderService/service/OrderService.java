package com.example.OrderService.service;

import com.example.OrderService.dto.OrderDto;

import java.util.concurrent.ExecutionException;

public interface OrderService {
    String createOrder(OrderDto orderDto) throws ExecutionException, InterruptedException;
}
