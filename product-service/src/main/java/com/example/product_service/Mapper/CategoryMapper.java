package com.example.product_service.Mapper;

import com.example.product_service.DTO.Request.CategoryRequest;
import com.example.product_service.DTO.Response.CategoryResponse;
import com.example.product_service.Model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/***
 * This is a mapper interface for Category entity
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);

    CategoryResponse toCategoryResponse(Category category);

    List<CategoryResponse> toListOfCategories(List<Category> categories);

    void updateCategoryDetails(@MappingTarget Category category, CategoryRequest request);
}
