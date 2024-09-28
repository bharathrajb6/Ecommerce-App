package com.example.product_service.validations;

import com.example.product_service.Exceptions.CategoryExceptions;
import com.example.product_service.Model.Category;

public class CategoryValidationHandler {
    public static void validateCategoryDetails(Category category) {
        if (category.getCategoryName() == null || category.getCategoryName().isEmpty() || category.getCategoryName().isBlank()) {
            throw new CategoryExceptions("Category name is empty or null.");
        }

        if (category.getCategoryDescription() == null || category.getCategoryDescription().isEmpty() || category.getCategoryDescription().isBlank()) {
            throw new CategoryExceptions("Category description is empty or null.");
        }

        if (category.getCategoryName().length() > 20) {
            throw new CategoryExceptions("Category is too long");
        }

        if (category.getCategoryDescription().length() > 200) {
            throw new CategoryExceptions("Category description is too long.");
        }
    }
}
