package com.example.user_service.Exceptions;

public class ProductException extends RuntimeException {

    public ProductException(String message) {
        super(message);
    }

    public ProductException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
