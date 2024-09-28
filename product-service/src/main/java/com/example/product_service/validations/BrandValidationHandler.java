package com.example.product_service.validations;

import com.example.product_service.Exceptions.BrandExceptions;
import com.example.product_service.Model.Brand;

public class BrandValidationHandler {
    public static void validateBrandDetails(Brand brand) {
        if (brand.getBrandName() == null || brand.getBrandName().isEmpty() || brand.getBrandName().isBlank()) {
            throw new BrandExceptions("Brand name is empty or null.");
        }

        if (brand.getBrandDescription() == null || brand.getBrandDescription().isEmpty() || brand.getBrandDescription().isBlank()) {
            throw new BrandExceptions("Brand description is empty or null.");
        }

        if (brand.getBrandName().length() > 20) {
            throw new BrandExceptions("Brand name is too long.");
        }

        if (brand.getBrandDescription().length() > 100) {
            throw new BrandExceptions("Brand description is too long.");
        }
    }
}
