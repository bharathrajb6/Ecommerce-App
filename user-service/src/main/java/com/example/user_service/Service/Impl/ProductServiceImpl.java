package com.example.user_service.Service.Impl;

import com.example.user_service.DTO.Request.ProductRequest;
import com.example.user_service.DTO.Response.ProductResponse;
import com.example.user_service.Exceptions.ProductException;
import com.example.user_service.Service.ProductService;
import feign.FeignException;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl {

    @Autowired
    private ProductService productService;

    public List<ProductResponse> getAllProducts() {
        try {
            return productService.getAllProducts();
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new ProductException(exception.getMessage());
            }
            throw new BadRequestException("Bad request while fetching product.");
        }
    }

    public ProductResponse getProduct(String prodID) {
        try {
            return productService.getProduct(prodID);
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new ProductException("Product not found with ID " + prodID);
            }
            throw new ProductException("Bad request while fetching product.");
        }
    }

    public List<ProductResponse> searchProduct(String criteria) {
        return productService.searchProduct(criteria);
    }

    public ProductResponse addProduct(ProductRequest request) {
        try{
            return productService.addProduct(request);
        }catch (FeignException exception){
            if (exception.status() == 404) {
                throw new ProductException("Resource not found");
            }
            throw new BadRequestException("Bad request while fetching product.");
        }
    }

    public ProductResponse updateProduct(String prodID, ProductRequest request) {
        return productService.updateProduct(prodID,request);
    }
}
