package com.example.product_service.validations;

import com.example.product_service.Exceptions.ProductExceptions;
import com.example.product_service.Model.Product;

public class ProductValidationHandler {

    public static void validateProductDetails(Product product) {
        if (product.getProdName() == null || product.getProdName().isEmpty() || product.getProdName().isBlank()) {
            throw new ProductExceptions("Product Name is empty or null.");
        }

        if (product.getProdDescription() == null || product.getProdDescription().isEmpty() || product.getProdDescription().isBlank()) {
            throw new ProductExceptions("Product Description is empty or null.");
        }

        if (product.getProdName().length() > 50) {
            throw new ProductExceptions("Product Name is too long.");
        }

        if (product.getProdDescription().length() > 500) {
            throw new ProductExceptions("Product Description is too long.");
        }

        if(product.getPrice()<=0){
            throw new ProductExceptions("Price cannot be zero or negative");
        }

        if(product.getStock()<=0){
            throw new ProductExceptions("Quantity cannot be zero or negative");
        }
    }
}
