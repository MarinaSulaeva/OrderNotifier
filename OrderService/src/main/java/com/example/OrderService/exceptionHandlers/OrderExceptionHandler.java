package com.example.OrderService.exceptionHandlers;

import com.example.OrderService.exception.ErrorMessage;
import com.example.OrderService.exception.SendException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class OrderExceptionHandler {
    @ExceptionHandler(value = {SendException.class})
    public ResponseEntity<?> handleSaveOrSendException(SendException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(new Date(), e.getMessage()));
    }
}
