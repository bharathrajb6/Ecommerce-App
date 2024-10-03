package com.example.user_service.Service;

import com.example.user_service.DTO.Request.ProductRequest;
import com.example.user_service.DTO.Response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse addProduct(ProductRequest request);

    ProductResponse getProduct(String value);

    List<ProductResponse> getAllProducts();

    ProductResponse updateProduct(String prodID, ProductRequest request);

    String deleteProduct(String value);

    List<ProductResponse> searchProduct(String criteria);
}
