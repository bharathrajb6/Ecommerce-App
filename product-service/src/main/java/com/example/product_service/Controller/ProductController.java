package com.example.product_service.Controller;

import com.example.product_service.DTO.Request.ProductRequest;
import com.example.product_service.DTO.Response.ApiResponse;
import com.example.product_service.DTO.Response.ProductResponse;
import com.example.product_service.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/")
public class ProductController {
    @Autowired
    ProductService productService;

    /***
     * This method is responsible for handling POST requests to add a new product.
     * @param request
     * @return
     */
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ApiResponse<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder().result(productService.addProduct(request)).build();
    }

    /***
     * This method is responsible for handling GET requests to get a product by its ID.
     * @param value
     * @return
     */
    @RequestMapping(value = "/product/{value}", method = RequestMethod.GET)
    public ApiResponse<ProductResponse> getProducts(@PathVariable String value) {
        return ApiResponse.<ProductResponse>builder().result(productService.getProduct(value)).build();
    }

    /***
     * This method is responsible for handling GET requests to get all products.
     * @return
     */
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        return ApiResponse.<List<ProductResponse>>builder().result(productService.getAllProducts()).build();
    }

    /***
     * This method is responsible for handling PUT requests to update a product.
     * @param prodID
     * @param request
     * @return
     */
    @RequestMapping(value = "/product/{productID}", method = RequestMethod.PUT)
    public ApiResponse<ProductResponse> updateProduct(@PathVariable String prodID, @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder().result(productService.updateProduct(prodID, request)).build();
    }

    /***
     * This method is responsible for handling DELETE requests to delete a product.
     * @param value
     * @return
     */
    @RequestMapping(value = "/product/{value}", method = RequestMethod.DELETE)
    public ApiResponse<Void> deleteProduct(@PathVariable String value) {
        return ApiResponse.<Void>builder().message(productService.deleteProduct(value)).build();
    }

    /***
     * This method is responsible for handling GET requests to get all products by category.
     * @param criteria
     * @return
     */
    @RequestMapping(value = "/product/search/{criteria}", method = RequestMethod.GET)
    public ApiResponse<List<ProductResponse>> searchProduct(@PathVariable String criteria) {
        return ApiResponse.<List<ProductResponse>>builder().result(productService.searchProduct(criteria)).build();
    }
}
