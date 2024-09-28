package com.example.product_service.Exceptions;

/***
 * This class is used to handle exceptions related to Brand
 */
public class BrandExceptions extends RuntimeException {
    public BrandExceptions(String message) {
        super(message);
    }

    public BrandExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
