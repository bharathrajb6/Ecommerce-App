package com.example.product_service.Exceptions;

/***
 * This class is used to handle exceptions related to Product
 */
public class ProductExceptions extends RuntimeException {

    public ProductExceptions(String message) {
        super(message);
    }

    public ProductExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
