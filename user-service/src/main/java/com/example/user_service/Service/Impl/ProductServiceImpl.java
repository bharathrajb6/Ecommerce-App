package com.example.user_service.Service.Impl;

import com.example.user_service.DTO.Request.ProductRequest;
import com.example.user_service.DTO.Response.ProductResponse;
import com.example.user_service.Exceptions.ProductException;
import com.example.user_service.Service.ProductService;
import feign.FeignException;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl {

    @Autowired
    private ProductService productService;

    /***
     * This method is used to get all the products used by both admin and user
     * @return
     */
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

    /***
     * This method is used to get the product by ID used by both admin and user
     * @param prodID
     * @return
     */
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

    /***
     * This method is used to search the product by criteria used by both admin and user
     * @param criteria
     * @return
     */
    public List<ProductResponse> searchProduct(String criteria) {
        return productService.searchProduct(criteria);
    }

    /***
     * This method is used to add the product used by admin
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse addProduct(ProductRequest request) {
        try {
            return productService.addProduct(request);
        } catch (FeignException exception) {
            if (exception.status() == 404) {
                throw new ProductException("Resource not found");
            }
            if (exception.status() == 400) {
                throw new ProductException(exception.getMessage());
            }
            throw new ProductException("Bad request while adding product.");
        }
    }

    /***
     * This method is used to update the product used by admin
     * @param prodID
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateProduct(String prodID, ProductRequest request) {
        return productService.updateProduct(prodID, request);
    }


    public String deleteProduct(String prodID) {
        return productService.deleteProduct(prodID);
    }

}
