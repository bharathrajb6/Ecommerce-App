package com.example.product_service.Service;

import com.example.product_service.DTO.Request.ProductRequest;
import com.example.product_service.DTO.Response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse addProduct(ProductRequest request);

    ProductResponse getProduct(String value);

    ProductResponse getProductByName(String prodName);

    List<ProductResponse> getAllProducts();

    ProductResponse updateProduct(String prodID, ProductRequest request);

    String deleteProduct(String value);

    String deleteProductByName(String productName);

    List<ProductResponse> searchProduct(String criteria);

    int getProductStock(String prodID);

    ProductResponse updateProductStock(String prodID,int newStock);
}
