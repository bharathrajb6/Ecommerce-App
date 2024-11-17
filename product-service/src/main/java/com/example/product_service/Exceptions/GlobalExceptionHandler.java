package com.example.product_service.Exceptions;

import com.example.product_service.DTO.Response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /***
     * This method is used to handle the exception thrown by the Brand Service
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(BrandExceptions.class)
    public ResponseEntity<?> handleBrandUpdateException(BrandExceptions ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Brand Error",
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /***
     * This method is used to handle the exception thrown by the Category Service
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(CategoryExceptions.class)
    public ResponseEntity<?> handleCategoryExceptions(CategoryExceptions ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Category Error",
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /***
     * This method is used to handle the exception thrown by the Product Service
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(ProductExceptions.class)
    public ResponseEntity<?> handleProductExceptions(ProductExceptions ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Product Error",
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
