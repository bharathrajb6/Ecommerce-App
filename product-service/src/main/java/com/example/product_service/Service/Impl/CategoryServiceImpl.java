package com.example.product_service.Service.Impl;

import com.example.product_service.DTO.Request.CategoryRequest;
import com.example.product_service.DTO.Response.CategoryResponse;
import com.example.product_service.Exceptions.CategoryExceptions;
import com.example.product_service.Mapper.CategoryMapper;
import com.example.product_service.Model.Category;
import com.example.product_service.Repository.CategoryRepository;
import com.example.product_service.Service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.example.product_service.Messages.Category.CategoryExceptionMessages.*;
import static com.example.product_service.Messages.Category.CategoryLogMessages.*;
import static com.example.product_service.Utils.CommonUtils.UUIDChecker;
import static com.example.product_service.validations.CategoryValidationHandler.validateCategoryDetails;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryMapper categoryMapper;
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    /***
     * Add a new category
     * @param request
     * @return
     */
    @Override
    public CategoryResponse addCategory(CategoryRequest request) {
        if (categoryRepository.existsByCategoryName(request.getCategoryName())) {
            logger.error(LOG_CATEGORY_WITH_SAME_NAME);
            throw new CategoryExceptions(EXCEPTION_CATEGORY_NAME_ALREADY_EXISTS);
        }
        Category category = categoryMapper.toCategory(request);
        category.setCategoryID(UUID.randomUUID().toString());
        category.setCategoryName(category.getCategoryName().toLowerCase());
        category.setActive(true);
        category.setCreated_at(Timestamp.from(Instant.now()));
        category.setUpdated_at(Timestamp.from(Instant.now()));
        validateCategoryDetails(category);
        try {
            category = categoryRepository.save(category);
            logger.info(LOG_CATEGORY_SAVED_SUCCESSFULLY, category.getCategoryName(), category.getCategoryID());
            return categoryMapper.toCategoryResponse(category);
        } catch (Exception e) {
            logger.error(LOG_UNABLE_TO_SAVE_CATEGORY, category.getCategoryName(), e.getMessage());
            throw new CategoryExceptions(String.format(EXCEPTION_UNABLE_TO_SAVE_CATEGORY, category.getCategoryName()));
        }
    }

    /***
     * Get a category by ID or Name
     * @param value
     * @return
     */
    @Override
    public CategoryResponse getCategory(String value) {
        if (UUIDChecker(value)) {
            Category category = categoryRepository.findByCategoryID(value).orElseThrow(() -> {
                logger.error(LOG_CATEGORY_NOT_FOUND_WITH_ID, value);
                return new CategoryExceptions(String.format(EXCEPTION_CATEGORY_NOT_FOUND, value));
            });
            return categoryMapper.toCategoryResponse(category);
        } else {
            Category category = categoryRepository.findByCategoryName(value).orElseThrow(() -> {
                logger.error(LOG_CATEGORY_NOT_FOUND_WITH_NAME, value);
                return new CategoryExceptions(String.format(EXCEPTION_CATEGORY_NOT_FOUND_WITH_NAME, value));
            });
            return categoryMapper.toCategoryResponse(category);
        }
    }

    /***
     * Get all categories
     * @return
     */
    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryMapper.toListOfCategories(categoryRepository.findAll());
    }


    /***
     * Update a category by name
     * @param categoryName
     * @param request
     * @return
     */
    @Override
    public CategoryResponse updateCategory(String categoryName, CategoryRequest request) {
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(() -> {
            logger.error(LOG_CATEGORY_NOT_FOUND_WITH_NAME, categoryName);
            return new CategoryExceptions(String.format(EXCEPTION_CATEGORY_NOT_FOUND_WITH_NAME, categoryName));
        });
        categoryMapper.updateCategoryDetails(category, request);
        try {
            categoryRepository.updateCategoryByID(category.getCategoryName(), category.getCategoryDescription(), Timestamp.from(Instant.now()), category.getCategoryID());
            logger.info(LOG_CATEGORY_UPDATED_SUCCESSFULLY, category.getCategoryName(), category.getCategoryID());
            return getCategory(categoryName);
        } catch (Exception e) {
            logger.error(LOG_UNABLE_TO_UPDATE_CATEGORY, category.getCategoryName(), e.getMessage());
            throw new CategoryExceptions(String.format(EXCEPTION_UNABLE_TO_UPDATE_CATEGORY, category.getCategoryName()), e);
        }
    }

    /***
     * Delete a category by ID or Name
     * @param value
     * @return
     */
    @Override
    public String deleteCategory(String value) {
        if (UUIDChecker(value)) {
            return categoryRepository.findByCategoryID(value).map(existingCategory -> {
                try {
                    categoryRepository.deleteByCategoryID(existingCategory.getCategoryID());
                    logger.info(LOG_CATEGORY_DELETED_SUCCESSFULLY, existingCategory.getCategoryName(), existingCategory.getCategoryID());
                    return CATEGORY_DELETED_SUCCESSFULLY;
                } catch (Exception e) {
                    logger.error(LOG_UNABLE_TO_DELETE_CATEGORY, existingCategory.getCategoryName(), e.getMessage());
                    throw new CategoryExceptions(EXCEPTION_UNABLE_TO_DELETE_CATEGORY);
                }
            }).orElseThrow(() -> {
                logger.error(LOG_CATEGORY_NOT_FOUND_WITH_ID, value);
                return new CategoryExceptions(String.format(EXCEPTION_CATEGORY_NOT_FOUND, value));
            });
        } else {
            return deleteCategoryByName(value);
        }
    }

    /***
     * Delete a category by name
     * @param value
     * @return
     */
    @Override
    public String deleteCategoryByName(String value) {
        return categoryRepository.findByCategoryName(value).map(existingCategory -> {
            try {
                categoryRepository.deleteByCategoryName(value);
                logger.info(LOG_CATEGORY_DELETED_SUCCESSFULLY, existingCategory.getCategoryName(), existingCategory.getCategoryID());
                return CATEGORY_DELETED_SUCCESSFULLY;
            } catch (Exception e) {
                logger.error(LOG_UNABLE_TO_DELETE_CATEGORY, existingCategory.getCategoryName(), e.getMessage());
                throw new CategoryExceptions(EXCEPTION_UNABLE_TO_DELETE_CATEGORY);
            }
        }).orElseThrow(() -> {
            logger.error(LOG_CATEGORY_NOT_FOUND_WITH_NAME, value);
            return new CategoryExceptions(String.format(EXCEPTION_CATEGORY_NOT_FOUND_WITH_NAME, value));
        });
    }
}
