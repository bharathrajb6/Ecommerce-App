package com.example.product_service.Controller;

import com.example.product_service.DTO.Request.CategoryRequest;
import com.example.product_service.DTO.Response.ApiResponse;
import com.example.product_service.DTO.Response.CategoryResponse;
import com.example.product_service.Model.Category;
import com.example.product_service.Service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /***
     * This method is responsible for handling POST requests to add a new category.
     * @param request
     * @return
     */
    @Operation(summary = "Add Category", description = "Add category to catalog")
    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public ApiResponse<CategoryResponse> addCategory(@RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder().result(categoryService.addCategory(request)).build();
    }

    /***
     * This the method used to handle get request to fetch brand.
     * @param value
     * @return
     */
    @Operation(summary = "Get Category", description = "Get category details from catalog")
    @RequestMapping(value = "/category/{value}", method = RequestMethod.GET)
    public ApiResponse<CategoryResponse> getCategory(@PathVariable String value) {
        return ApiResponse.<CategoryResponse>builder().result(categoryService.getCategory(value)).build();
    }

    /***
     * This method is responsible for handling GET requests to fetch all category details.
     * @return
     */
    @Operation(summary = "Get All Categories", description = "Get all categories from catalog")
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return ApiResponse.<List<CategoryResponse>>builder().result(categoryService.getAllCategories()).build();
    }

    /***
     * This method is responsible for handling PUT requests to update the category details.
     * @param categoryId
     * @param category
     * @return
     */
    @Operation(summary = "Update Category", description = "Update category details in catalog")
    @RequestMapping(value = "/category/{categoryID}", method = RequestMethod.PUT)
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable String categoryId, @RequestBody Category category) {
        return ApiResponse.<CategoryResponse>builder().result(categoryService.updateCategory(categoryId, CategoryRequest.builder().build())).build();
    }

    /***
     * This method is responsible for handling DELETE requests to delete the category.
     * @param value
     * @return
     */
    @Operation(summary = "Delete Category", description = "Delete the category from catalog")
    @RequestMapping(value = "/category/{value}", method = RequestMethod.DELETE)
    public ApiResponse<CategoryResponse> deleteCategory(@PathVariable String value) {
        return ApiResponse.<CategoryResponse>builder().message(categoryService.deleteCategory(value)).build();
    }
}
