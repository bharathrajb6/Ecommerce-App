package com.example.order_service.Exception;

/***
 * OrderException class
 */
public class OrderException extends RuntimeException {

    public OrderException(String message){
        super(message);
    }

    public OrderException(String message,Throwable cause){
        super(message,cause);
    }
}
