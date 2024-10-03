package com.example.user_service.Controller;

import com.example.user_service.DTO.Request.ProductRequest;
import com.example.user_service.DTO.Response.ApiResponse;
import com.example.user_service.DTO.Response.ProductResponse;
import com.example.user_service.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ApiResponse<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder().result(productService.addProduct(request)).build();
    }

    @RequestMapping(value = "/product/{prodID}", method = RequestMethod.PUT)
    public ApiResponse<ProductResponse> updateProduct(@PathVariable String prodID, @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder().result(productService.updateProduct(prodID, request)).build();
    }
}
