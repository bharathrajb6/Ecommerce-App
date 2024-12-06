package com.example.product_service.Service.Impl;

import com.example.product_service.DTO.Request.CategoryRequest;
import com.example.product_service.DTO.Response.CategoryResponse;
import com.example.product_service.Exceptions.CategoryExceptions;
import com.example.product_service.Mapper.CategoryMapper;
import com.example.product_service.Model.Category;
import com.example.product_service.Repository.CategoryRepository;
import com.example.product_service.Service.CategoryService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisServiceImpl redisService;

    /***
     * Add a new category
     * @param request
     * @return
     */
    @Override
    public CategoryResponse addCategory(CategoryRequest request) {
        // Check if category name is already exists
        if (categoryRepository.existsByCategoryName(request.getCategoryName())) {
            log.error(LOG_CATEGORY_WITH_SAME_NAME);
            // Throw if category is already exists in database
            throw new CategoryExceptions(EXCEPTION_CATEGORY_NAME_ALREADY_EXISTS);
        }

        Category category = categoryMapper.toCategory(request);

        category.setCategoryID(UUID.randomUUID().toString());
        category.setCategoryName(category.getCategoryName());
        category.setActive(true);

        Timestamp currentTimeStamp = Timestamp.from(Instant.now());
        category.setCreated_at(currentTimeStamp);
        category.setUpdated_at(currentTimeStamp);

        validateCategoryDetails(category);

        try {
            // Save the category information to database
            category = categoryRepository.save(category);

            // Delete all category data from cache
            redisService.deleteData("categoryList");
            log.info(LOG_CATEGORY_SAVED_SUCCESSFULLY, category.getCategoryName(), category.getCategoryID());
            return getCategory(category.getCategoryID());
        } catch (Exception e) {
            // If any issue occured then throw category exception
            log.error(LOG_UNABLE_TO_SAVE_CATEGORY, category.getCategoryName(), e.getMessage());
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
        // Check if the value is UUID
        if (UUIDChecker(value)) {
            // Check if category information is present in cache
            CategoryResponse categoryResponse = redisService.getData(value, CategoryResponse.class);
            if (categoryResponse != null) {
                return categoryResponse;
            } else {
                // If it is not present in cache, then load it from database
                Category category = categoryRepository.findByCategoryID(value).orElseThrow(() -> {
                    log.error(LOG_CATEGORY_NOT_FOUND_WITH_ID, value);
                    return new CategoryExceptions(String.format(EXCEPTION_CATEGORY_NOT_FOUND, value));
                });

                // Add category information to cache
                redisService.setData(value, category, 300L);
                return categoryMapper.toCategoryResponse(category);
            }
        } else {
            return getCategoryByName(value);
        }
    }

    @Override
    public CategoryResponse getCategoryByName(String value) {
        // Check if category information is present in cache
        CategoryResponse categoryResponse = redisService.getData(value, CategoryResponse.class);
        if (categoryResponse != null) {
            return categoryResponse;
        } else {
            // If it is not present in cache, then load it from database
            Category category = categoryRepository.findByCategoryName(value).orElseThrow(() -> {
                log.error(LOG_CATEGORY_NOT_FOUND_WITH_NAME, value);
                return new CategoryExceptions(String.format(EXCEPTION_CATEGORY_NOT_FOUND_WITH_NAME, value));
            });

            // Add category information to cache
            redisService.setData(value, category, 300L);
            return categoryMapper.toCategoryResponse(category);
        }
    }

    /***
     * Get all categories
     * @return
     */
    @Override
    public List<CategoryResponse> getAllCategories() {
        // Get all categories from cache
        List<CategoryResponse> categoryResponses = redisService.getData("categoryList", List.class);
        if (categoryResponses != null) {
            return categoryResponses;
        } else {
            // If it is not present in cache, then load it from database
            List<Category> categories = categoryRepository.findAll();

            // Add all category information to cache
            redisService.setData("categoryList", categories, 300L);
            return categoryMapper.toListOfCategories(categories);
        }
    }


    /***
     * Update a category by name
     * @param categoryName
     * @param request
     * @return
     */
    @Override
    public CategoryResponse updateCategory(String categoryName, CategoryRequest request) {
        // Check if category is present or not with this same
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(() -> {
            log.error(LOG_CATEGORY_NOT_FOUND_WITH_NAME, categoryName);
            return new CategoryExceptions(String.format(EXCEPTION_CATEGORY_NOT_FOUND_WITH_NAME, categoryName));
        });

        categoryMapper.updateCategoryDetails(category, request);
        try {
            // Update the category with latest information
            categoryRepository.updateCategoryByID(category.getCategoryName(), category.getCategoryDescription(), Timestamp.from(Instant.now()), category.getCategoryID());
            log.info(LOG_CATEGORY_UPDATED_SUCCESSFULLY, category.getCategoryName(), category.getCategoryID());

            // Delete the category information from cache
            redisService.deleteData(categoryName);
            redisService.deleteData(category.getCategoryID());
            return getCategory(categoryName);
        } catch (Exception e) {
            log.error(LOG_UNABLE_TO_UPDATE_CATEGORY, category.getCategoryName(), e.getMessage());
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
        // Check if the value is UUID
        if (UUIDChecker(value)) {
            return categoryRepository.findByCategoryID(value).map(existingCategory -> {
                try {
                    // Delete the category from database using category ID
                    categoryRepository.deleteByCategoryID(existingCategory.getCategoryID());

                    // Delete the data from cache
                    redisService.deleteData(value);

                    log.info(LOG_CATEGORY_DELETED_SUCCESSFULLY, existingCategory.getCategoryName(), existingCategory.getCategoryID());
                    return CATEGORY_DELETED_SUCCESSFULLY;
                } catch (Exception e) {
                    log.error(LOG_UNABLE_TO_DELETE_CATEGORY, existingCategory.getCategoryName(), e.getMessage());
                    throw new CategoryExceptions(EXCEPTION_UNABLE_TO_DELETE_CATEGORY);
                }
            }).orElseThrow(() -> {
                log.error(LOG_CATEGORY_NOT_FOUND_WITH_ID, value);

                // Throw the error if category is not present with this ID
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
                // Delete the category from database using category name
                categoryRepository.deleteByCategoryName(value);

                // Delete the data from cache
                redisService.deleteData(value);

                log.info(LOG_CATEGORY_DELETED_SUCCESSFULLY, existingCategory.getCategoryName(), existingCategory.getCategoryID());
                return CATEGORY_DELETED_SUCCESSFULLY;
            } catch (Exception e) {
                log.error(LOG_UNABLE_TO_DELETE_CATEGORY, existingCategory.getCategoryName(), e.getMessage());
                throw new CategoryExceptions(EXCEPTION_UNABLE_TO_DELETE_CATEGORY);
            }
        }).orElseThrow(() -> {
            log.error(LOG_CATEGORY_NOT_FOUND_WITH_NAME, value);

            // Throw the error if category is not present with this name
            return new CategoryExceptions(String.format(EXCEPTION_CATEGORY_NOT_FOUND_WITH_NAME, value));
        });
    }
}
