package com.example.user_service.Service.Impl;

import com.example.user_service.DTO.Request.ProductRequest;
import com.example.user_service.DTO.Response.Product.ProductResponse;
import com.example.user_service.Exceptions.ProductException;
import com.example.user_service.Service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl {

    @Autowired
    private ProductService productService;

    /***
     * This method is used to get all the products used by both admin and user
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductResponse> getAllProducts() {
        try {
            return productService.getAllProducts();
        } catch (ProductException exception) {
            log.error(exception.getMessage());
            throw new ProductException(exception.getMessage());
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
        } catch (ProductException exception) {
            log.error(exception.getMessage());
            throw new ProductException(exception.getMessage());
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
    @Transactional
    public ProductResponse addProduct(ProductRequest request) {
        try {
            return productService.addProduct(request);
        } catch (ProductException productException) {
            log.error(productException.getMessage());
            throw new ProductException(productException.getMessage());
        }
    }

    /***
     * This method is used to update the product used by admin
     * @param prodID
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductResponse updateProduct(String prodID, ProductRequest request) {
        try {
            return productService.updateProduct(prodID, request);
        } catch (ProductException productException) {
            log.error(productException.getMessage());
            throw new ProductException(productException.getMessage());
        }
    }


    /**
     * This method is used to delete the product from catalog.
     *
     * @param prodID
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public String deleteProduct(String prodID) {
        try {
            return productService.deleteProduct(prodID);
        } catch (ProductException productException) {
            log.error(productException.getMessage());
            throw new ProductException(productException.getMessage());
        }
    }

}
