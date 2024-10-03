package com.example.user_service.Service.Impl;

import com.example.user_service.DTO.Request.ProductRequest;
import com.example.user_service.DTO.Response.ProductResponse;
import com.example.user_service.Exceptions.ProductException;
import com.example.user_service.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private RestTemplate restTemplate;

    private static final String url = "http://localhost:8080/api/v1/product";
    private static final String slash = "/";

    @Override
    public ProductResponse addProduct(ProductRequest request) {
        try {
            return restTemplate.postForObject(url, request, ProductResponse.class);
        } catch (Exception e) {
            throw new ProductException(e.getMessage());
        }
    }

    @Override
    public ProductResponse getProduct(String value) {
        try {
            return restTemplate.exchange(url + slash + value, HttpMethod.GET, null, new ParameterizedTypeReference<ProductResponse>() {
            }).getBody();
        } catch (Exception e) {
            throw new ProductException(e.getMessage());
        }
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        try {
            return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductResponse>>() {
            }).getBody();
        } catch (Exception ex) {
            throw new ProductException(ex.getMessage());
        }
    }

    @Override
    public ProductResponse updateProduct(String prodID, ProductRequest request) {
        HttpEntity<ProductRequest> entity = new HttpEntity<>(request);
        try {
            ResponseEntity<ProductResponse> response = restTemplate.exchange(url + slash + prodID, HttpMethod.PUT, entity, ProductResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new ProductException(e.getMessage());
        }
    }

    @Override
    public String deleteProduct(String value) {
        try {
            return restTemplate.exchange(url + slash + value, HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
            }).getBody();
        } catch (Exception e) {
            throw new ProductException(e.getMessage());
        }
    }

    @Override
    public List<ProductResponse> searchProduct(String criteria) {
        String searchURL = url + slash + "search" + slash + criteria;
        try {
            return restTemplate.exchange(searchURL, HttpMethod.GET, null, new ParameterizedTypeReference<List<ProductResponse>>() {
            }).getBody();
        } catch (Exception e) {
            throw new ProductException(e.getMessage());
        }
    }
}
