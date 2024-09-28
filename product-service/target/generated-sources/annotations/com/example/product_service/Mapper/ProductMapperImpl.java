package com.example.product_service.Mapper;

import com.example.product_service.DTO.Request.ProductRequest;
import com.example.product_service.DTO.Response.ProductResponse;
import com.example.product_service.Model.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-28T22:11:36+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Azul Systems, Inc.)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toProduct(ProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product product = new Product();

        product.setProdName( request.getProdName() );
        product.setProdDescription( request.getProdDescription() );
        product.setCategory( request.getCategory() );
        product.setPrice( request.getPrice() );
        product.setDiscountPrice( request.getDiscountPrice() );
        product.setStock( request.getStock() );
        product.setCurrency( request.getCurrency() );
        product.setBrand( request.getBrand() );
        product.setWeight( request.getWeight() );
        product.setDimensions( request.getDimensions() );
        product.setColor( request.getColor() );
        product.setSize( request.getSize() );
        product.setMaterial( request.getMaterial() );

        return product;
    }

    @Override
    public ProductResponse toProductResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.prodID( product.getProdID() );
        productResponse.prodName( product.getProdName() );
        productResponse.prodDescription( product.getProdDescription() );
        productResponse.category( product.getCategory() );
        productResponse.price( product.getPrice() );
        productResponse.discountPrice( product.getDiscountPrice() );
        productResponse.stock( product.getStock() );
        productResponse.currency( product.getCurrency() );
        productResponse.brand( product.getBrand() );
        productResponse.weight( product.getWeight() );
        productResponse.dimensions( product.getDimensions() );
        productResponse.color( product.getColor() );
        productResponse.size( product.getSize() );
        productResponse.material( product.getMaterial() );
        productResponse.created_at( product.getCreated_at() );
        productResponse.updated_at( product.getUpdated_at() );

        return productResponse.build();
    }

    @Override
    public List<ProductResponse> toProductResponseList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductResponse> list = new ArrayList<ProductResponse>( products.size() );
        for ( Product product : products ) {
            list.add( toProductResponse( product ) );
        }

        return list;
    }

    @Override
    public void updateProductStock(Product product, ProductRequest request) {
        if ( request == null ) {
            return;
        }

        product.setProdName( request.getProdName() );
        product.setProdDescription( request.getProdDescription() );
        product.setCategory( request.getCategory() );
        product.setPrice( request.getPrice() );
        product.setDiscountPrice( request.getDiscountPrice() );
        product.setStock( request.getStock() );
        product.setCurrency( request.getCurrency() );
        product.setBrand( request.getBrand() );
        product.setWeight( request.getWeight() );
        product.setDimensions( request.getDimensions() );
        product.setColor( request.getColor() );
        product.setSize( request.getSize() );
        product.setMaterial( request.getMaterial() );
    }
}
