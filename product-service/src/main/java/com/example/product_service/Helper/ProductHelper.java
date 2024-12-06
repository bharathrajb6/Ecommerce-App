package com.example.product_service.Helper;

import com.example.product_service.DTO.Request.Product.ProductRequest;
import com.example.product_service.DTO.Response.BrandResponse;
import com.example.product_service.DTO.Response.CategoryResponse;
import com.example.product_service.DTO.Response.Product.ProductBrandResponse;
import com.example.product_service.DTO.Response.Product.ProductCategoryResponse;
import com.example.product_service.DTO.Response.Product.ProductResponse;
import com.example.product_service.Exceptions.ProductExceptions;
import com.example.product_service.Model.Brand;
import com.example.product_service.Model.Category;
import com.example.product_service.Model.Product;
import com.example.product_service.Repository.BrandRepository;
import com.example.product_service.Repository.CategoryRepository;
import com.example.product_service.Service.Impl.RedisServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.example.product_service.Messages.Brand.BrandExceptionMessages.EXCEPTION_BRAND_NOT_FOUND_WITH_ID;
import static com.example.product_service.Messages.Brand.BrandLogMessages.LOG_BRAND_NOT_FOUND_WITH_NAME;
import static com.example.product_service.Messages.Category.CategoryExceptionMessages.EXCEPTION_CATEGORY_NOT_FOUND;
import static com.example.product_service.Messages.Category.CategoryLogMessages.LOG_CATEGORY_NOT_FOUND_WITH_NAME;

@Component
@Slf4j
public class ProductHelper {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private RedisServiceImpl redisService;

    public Product toProduct(ProductRequest request) {

        // Check if category is present
        String categoryID = getCategoryDetails(request.getCategory().getId()).getId();

        // Check if brand is present
        String brandID = getBrandDetails(request.getBrand().getId()).getId();

        Product product = new Product();
        product.setProdID(UUID.randomUUID().toString());
        product.setProdName(request.getProdName());
        product.setProdDescription(request.getProdDescription());
        product.setCategory(categoryID);
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setStock(request.getStock());
        product.setCurrency(request.getCurrency());
        product.setBrand(brandID);
        product.setWeight(request.getWeight());
        product.setDimensions(request.getDimensions());
        product.setColor(request.getColor());
        product.setSize(request.getSize());
        product.setMaterial(request.getMaterial());

        return product;
    }

    public ProductResponse toProductResponse(Product product) {

        // Check if category is present
        ProductCategoryResponse categoryResponse = getCategoryDetails(product.getCategory());

        // Check if brand is present
        ProductBrandResponse brandResponse = getBrandDetails(product.getBrand());

        ProductResponse productResponse = new ProductResponse();

        productResponse.setProdID(product.getProdID());
        productResponse.setProdName(product.getProdName());
        productResponse.setProdDescription(product.getProdDescription());
        productResponse.setCategory(categoryResponse);
        productResponse.setBrand(brandResponse);
        productResponse.setPrice(product.getPrice());
        productResponse.setDiscountPrice(product.getDiscountPrice());
        productResponse.setStock(product.getStock());
        productResponse.setCurrency(product.getCurrency());
        productResponse.setWeight(product.getWeight());
        productResponse.setDimensions(product.getDimensions());
        productResponse.setColor(product.getColor());
        productResponse.setSize(product.getSize());
        productResponse.setMaterial(product.getMaterial());
        productResponse.setCreated_at(product.getCreated_at());
        productResponse.setUpdated_at(product.getUpdated_at());

        return productResponse;
    }


    public ProductCategoryResponse getCategoryDetails(String category) {
        CategoryResponse categoryResponse = redisService.getData(category, CategoryResponse.class);
        if (categoryResponse != null) {
            return ProductCategoryResponse.builder().id(categoryResponse.getCategoryID()).name(categoryResponse.getCategoryName()).build();
        } else {
            // Check if category is present
            Category fetchedFromDB = categoryRepository.findByCategoryID(category).orElseThrow(() -> {
                log.error(LOG_CATEGORY_NOT_FOUND_WITH_NAME, category);
                return new ProductExceptions(String.format(EXCEPTION_CATEGORY_NOT_FOUND, category));
            });
            redisService.setData(fetchedFromDB.getCategoryID(), fetchedFromDB, 300L);
            return ProductCategoryResponse.builder().id(fetchedFromDB.getCategoryID()).name(fetchedFromDB.getCategoryName()).build();
        }
    }


    public ProductBrandResponse getBrandDetails(String brand) {
        BrandResponse brandResponse = redisService.getData(brand, BrandResponse.class);
        if (brandResponse != null) {
            return ProductBrandResponse.builder().id(brandResponse.getBrandID()).name(brandResponse.getBrandName()).build();
        } else {
            // Check if brand is present in DB
            Brand fetchedFromDB = brandRepository.findByBrandID(brand).orElseThrow(() -> {
                log.error(LOG_BRAND_NOT_FOUND_WITH_NAME, brand);
                return new ProductExceptions(String.format(EXCEPTION_BRAND_NOT_FOUND_WITH_ID, brand));
            });
            redisService.setData(fetchedFromDB.getBrandID(), fetchedFromDB, 300L);
            return ProductBrandResponse.builder().id(fetchedFromDB.getBrandID()).name(fetchedFromDB.getBrandName()).build();
        }
    }
}
