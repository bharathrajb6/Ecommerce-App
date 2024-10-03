package com.example.order_service.Service;


import com.example.order_service.DTO.Response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductService {
    @RequestMapping(value = "/api/v1/product/{value}", method = RequestMethod.GET)
    ProductResponse getProduct(@PathVariable String value);

    @RequestMapping(value = "/api/v1/product", method = RequestMethod.GET)
    List<ProductResponse> getAllProducts();

    @RequestMapping(value = "/product/search/{criteria}", method = RequestMethod.GET)
    List<ProductResponse> searchProduct(@PathVariable String criteria);
}
