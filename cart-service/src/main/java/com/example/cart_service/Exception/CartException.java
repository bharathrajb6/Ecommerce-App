package com.example.cart_service.Exception;

public class CartException extends RuntimeException {
    public CartException(String message) {
        super(message);
    }

    public CartException(String message,Throwable cause){
        super(message,cause);
    }
}
