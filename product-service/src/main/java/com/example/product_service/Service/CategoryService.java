package com.example.product_service.Service;

import com.example.product_service.DTO.Request.CategoryRequest;
import com.example.product_service.DTO.Response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse addCategory(CategoryRequest request);

    CategoryResponse getCategory(String value);

    CategoryResponse getCategoryByName(String value);

    List<CategoryResponse> getAllCategories();

    CategoryResponse updateCategory(String categoryName, CategoryRequest request);

    String deleteCategory(String value);

    String deleteCategoryByName(String value);
}
