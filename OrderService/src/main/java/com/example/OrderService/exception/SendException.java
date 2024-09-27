package com.example.OrderService.exception;

public class SendException extends RuntimeException{
    public SendException(Throwable cause) {
        super(cause);
    }
}
