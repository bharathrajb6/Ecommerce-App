package com.example.product_service.Mapper;

import com.example.product_service.DTO.Request.CategoryRequest;
import com.example.product_service.DTO.Response.CategoryResponse;
import com.example.product_service.Model.Category;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-28T22:11:36+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Azul Systems, Inc.)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toCategory(CategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        Category category = new Category();

        category.setCategoryName( request.getCategoryName() );
        category.setCategoryDescription( request.getCategoryDescription() );
        category.setActive( request.isActive() );
        category.setMeta_title( request.getMeta_title() );
        category.setMeta_description( request.getMeta_description() );
        category.setMeta_keywords( request.getMeta_keywords() );

        return category;
    }

    @Override
    public CategoryResponse toCategoryResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();

        categoryResponse.categoryID( category.getCategoryID() );
        categoryResponse.categoryName( category.getCategoryName() );
        categoryResponse.categoryDescription( category.getCategoryDescription() );
        categoryResponse.created_at( category.getCreated_at() );
        categoryResponse.updated_at( category.getUpdated_at() );
        categoryResponse.meta_title( category.getMeta_title() );
        categoryResponse.meta_description( category.getMeta_description() );
        categoryResponse.meta_keywords( category.getMeta_keywords() );

        return categoryResponse.build();
    }

    @Override
    public List<CategoryResponse> toListOfCategories(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryResponse> list = new ArrayList<CategoryResponse>( categories.size() );
        for ( Category category : categories ) {
            list.add( toCategoryResponse( category ) );
        }

        return list;
    }

    @Override
    public void updateCategoryDetails(Category category, CategoryRequest request) {
        if ( request == null ) {
            return;
        }

        category.setCategoryName( request.getCategoryName() );
        category.setCategoryDescription( request.getCategoryDescription() );
        category.setActive( request.isActive() );
        category.setMeta_title( request.getMeta_title() );
        category.setMeta_description( request.getMeta_description() );
        category.setMeta_keywords( request.getMeta_keywords() );
    }
}
