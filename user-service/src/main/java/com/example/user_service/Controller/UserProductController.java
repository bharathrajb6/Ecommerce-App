package com.example.user_service.Controller;

import com.example.user_service.DTO.Response.ApiResponse;
import com.example.user_service.DTO.Response.ProductResponse;
import com.example.user_service.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ApiResponse<List<ProductResponse>> getProducts() {
        return ApiResponse.<List<ProductResponse>>builder().result(productService.getAllProducts()).build();
    }

    @RequestMapping(value = "/product/{prodID}", method = RequestMethod.GET)
    public ApiResponse<ProductResponse> getProduct(@PathVariable String prodID) {
        return ApiResponse.<ProductResponse>builder().result(productService.getProduct(prodID)).build();
    }

    @RequestMapping(value = "/product/search/{criteria}",method = RequestMethod.GET)
    public ApiResponse<List<ProductResponse>> searchProducts(@PathVariable String criteria){
        return ApiResponse.<List<ProductResponse>>builder().result(productService.searchProduct(criteria)).build();
    }
}
