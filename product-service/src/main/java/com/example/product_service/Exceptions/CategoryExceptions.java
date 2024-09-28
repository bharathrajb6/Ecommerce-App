package com.example.product_service.Exceptions;

/***
 * This class is used to handle exceptions related to Category
 */
public class CategoryExceptions extends RuntimeException {

    public CategoryExceptions(String message) {
        super(message);
    }

    public CategoryExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
