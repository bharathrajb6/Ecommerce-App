package com.example.product_service.Mapper;

import com.example.product_service.DTO.Request.ProductRequest;
import com.example.product_service.DTO.Response.ProductResponse;
import com.example.product_service.Model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/***
 * This is a mapper interface for Product entity
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(ProductRequest request);

    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> products);

    void updateProductStock(@MappingTarget Product product, ProductRequest request);
}
