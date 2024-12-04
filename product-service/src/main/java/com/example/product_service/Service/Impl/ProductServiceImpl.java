package com.example.product_service.Service.Impl;

import com.example.product_service.DTO.Request.ProductRequest;
import com.example.product_service.DTO.Response.ProductResponse;
import com.example.product_service.Exceptions.ProductExceptions;
import com.example.product_service.Mapper.ProductMapper;
import com.example.product_service.Model.Brand;
import com.example.product_service.Model.Category;
import com.example.product_service.Model.Product;
import com.example.product_service.Repository.BrandRepository;
import com.example.product_service.Repository.CategoryRepository;
import com.example.product_service.Repository.ProductRepository;
import com.example.product_service.Service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.example.product_service.Messages.Brand.BrandExceptionMessages.EXCEPTION_BRAND_NOT_FOUND_WITH_NAME;
import static com.example.product_service.Messages.Brand.BrandLogMessages.LOG_BRAND_NOT_FOUND_WITH_NAME;
import static com.example.product_service.Messages.Category.CategoryExceptionMessages.EXCEPTION_CATEGORY_NOT_FOUND_WITH_NAME;
import static com.example.product_service.Messages.Category.CategoryLogMessages.LOG_CATEGORY_NOT_FOUND_WITH_NAME;
import static com.example.product_service.Messages.Product.ProductExceptionMessages.*;
import static com.example.product_service.Messages.Product.ProductLogMessages.*;
import static com.example.product_service.Utils.CommonUtils.UUIDChecker;
import static com.example.product_service.validations.ProductValidationHandler.validateProductDetails;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RedisServiceImpl redisService;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    /***
     * This method is used to add the product.
     * @param request
     * @return
     */
    @Override
    public ProductResponse addProduct(ProductRequest request) {
        Product product = productMapper.toProduct(request);
        if (productRepository.existsByProdName(product.getProdName())) {
            logger.error(LOG_PRODUCT_ALREADY_EXISTS, product.getProdName());
            throw new ProductExceptions(String.format(EXCEPTION_PRODUCT_ALREADY_FOUND, product.getProdName()));
        }
        validateProductDetails(product);
        product.setProdID(UUID.randomUUID().toString());
        Category category = categoryRepository.findByCategoryName(product.getCategory().toLowerCase()).orElseThrow(() -> {
            logger.error(LOG_CATEGORY_NOT_FOUND_WITH_NAME, product.getCategory());
            return new ProductExceptions(String.format(EXCEPTION_CATEGORY_NOT_FOUND_WITH_NAME, product.getCategory()));
        });
        Brand brand = brandRepository.findByBrandName(product.getBrand().toLowerCase()).orElseThrow(() -> {
            logger.error(LOG_BRAND_NOT_FOUND_WITH_NAME, product.getBrand());
            return new ProductExceptions(EXCEPTION_BRAND_NOT_FOUND_WITH_NAME);
        });
        try {
            product.setCategory(category.getCategoryID());
            product.setBrand(brand.getBrandID());
            Timestamp currentTimestamp = Timestamp.from(Instant.now());
            product.setCreated_at(currentTimestamp);
            product.setUpdated_at(currentTimestamp);
            productRepository.save(product);
            logger.info(LOG_PRODUCT_SAVED_SUCCESSFULLY, product.getProdName(), product.getProdID());
            return getProduct(product.getProdID());
        } catch (Exception e) {
            logger.error(LOG_UNABLE_TO_SAVE_PRODUCT, product.getProdName(), e.getMessage());
            throw new ProductExceptions(String.format(EXCEPTION_UNABLE_TO_SAVE_PRODUCT, product.getProdName()));
        }
    }

    /***
     * This method will return the product entity based on product ID.
     * @param value
     * @return
     */
    @Override
    public ProductResponse getProduct(String value) {
        if (UUIDChecker(value)) {
            ProductResponse productResponse = redisService.getData(value, ProductResponse.class);
            if (productResponse != null) {
                return productResponse;
            } else {
                return productRepository.findByProdID(value).map(product -> {
                    Brand brand = brandRepository.findByBrandID(product.getBrand()).orElse(null);
                    Category category = categoryRepository.findByCategoryID(product.getCategory()).orElse(null);
                    logger.info(LOG_PRODUCT_FETCHED_FROM_DB, product.getProdName());
                    product.setCategory(category.getCategoryName());
                    product.setBrand(brand.getBrandName());
                    redisService.setData(value, product, 300L);
                    return productMapper.toProductResponse(product);
                }).orElseThrow(() -> {
                    logger.error(LOG_PRODUCT_NOT_FOUND_WITH_ID, value);
                    return new ProductExceptions("Product not found with ID: " + value);
                });
            }
        } else {
            return getProductByName(value);
        }
    }

    /***
     * This method will return the product entity based on product name.
     * @param prodName
     * @return
     */
    @Override
    public ProductResponse getProductByName(String prodName) {
        ProductResponse productResponse = redisService.getData(prodName, ProductResponse.class);
        if (productResponse != null) {
            return productResponse;
        } else {
            return productRepository.findByProdName(prodName).map(product -> {
                Brand brand = brandRepository.findByBrandID(product.getBrand()).orElse(null);
                Category category = categoryRepository.findByCategoryID(product.getCategory()).orElse(null);
                logger.info(LOG_PRODUCT_FETCHED_FROM_DB, product.getProdName());
                product.setCategory(category.getCategoryName());
                product.setBrand(brand.getBrandName());
                redisService.setData(prodName, product, 300L);
                return productMapper.toProductResponse(product);
            }).orElseThrow(() -> {
                logger.error(LOG_PRODUCT_NOT_FOUND_WITH_NAME, prodName);
                return new ProductExceptions(String.format(EXCEPTION_PRODUCT_NOT_FOUND_WITH_NAME, prodName));
            });
        }
    }

    /***
     * This method will return the all the product entities from database.
     * @return
     */
    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findAll().stream().map(product -> {
            Brand brand = brandRepository.findByBrandID(product.getBrand()).orElse(null);
            Category category = categoryRepository.findByCategoryID(product.getCategory()).orElse(null);
            logger.info(LOG_PRODUCT_FETCHED_FROM_DB, product.getProdName());
            product.setCategory(category.getCategoryName());
            product.setBrand(brand.getBrandName());
            return product;
        }).toList();
        return productMapper.toProductResponseList(productList);
    }

    /**
     * This method will update the product stock based on product ID.
     *
     * @param prodID
     * @param request
     * @return
     */
    @Override
    public ProductResponse updateProduct(String prodID, ProductRequest request) {
        Product product = productRepository.findByProdID(prodID).orElseThrow(() -> {
            logger.error(LOG_PRODUCT_NOT_FOUND_WITH_ID, prodID);
            return new ProductExceptions(String.format(EXCEPTION_PRODUCT_NOT_FOUND_WITH_ID, prodID));
        });
        productMapper.updateProductStock(product, request);
        int result = productRepository.updateProductByID(product.getDimensions(), product.getPrice(), product.getDiscountPrice(), product.getStock(), Timestamp.from(Instant.now()), prodID);
        logger.info(LOG_PRODUCT_STOCK_UPDATED_SUCCESSFULLY, product.getProdName(), product.getProdID());
        return productMapper.toProductResponse(product);
    }

    /***
     * Deletes the product entity based on product ID.
     * @param value
     * @return
     */
    @Override
    public String deleteProduct(String value) {
        if (UUIDChecker(value)) {
            return productRepository.findByProdID(value).map(product -> {
                try {
                    productRepository.delete(product);
                    logger.info(LOG_PRODUCT_DELETED_SUCCESSFULLY, product.getProdName(), product.getProdID());
                    return PRODUCT_DELETED_SUCCESSFULLY;
                } catch (Exception e) {
                    logger.error(LOG_UNABLE_TO_DELETE_PRODUCT, product.getProdName(), product.getProdID());
                    throw new ProductExceptions(String.format(EXCEPTION_UNABLE_TO_DELETE_PRODUCT, product.getProdName()));
                }
            }).orElseThrow(() -> {
                logger.error(LOG_PRODUCT_NOT_FOUND_WITH_ID, value);
                return new ProductExceptions(String.format(EXCEPTION_PRODUCT_NOT_FOUND_WITH_ID, value));
            });
        } else {
            return deleteProductByName(value);
        }
    }

    /***
     * Deletes the product entity based on product name.
     * @param productName
     * @return
     */
    @Override
    public String deleteProductByName(String productName) {
        return productRepository.findByProdName(productName).map(product -> {
            try {
                productRepository.delete(product);
                logger.info(LOG_PRODUCT_DELETED_SUCCESSFULLY, product.getProdName(), product.getProdID());
                return PRODUCT_DELETED_SUCCESSFULLY;
            } catch (Exception e) {
                logger.error(LOG_UNABLE_TO_DELETE_PRODUCT, product.getProdName(), product.getProdID());
                throw new ProductExceptions(String.format(EXCEPTION_UNABLE_TO_DELETE_PRODUCT, product.getProdName()));
            }
        }).orElseThrow(() -> {
            logger.error(LOG_PRODUCT_NOT_FOUND_WITH_NAME, productName);
            return new ProductExceptions(String.format(EXCEPTION_PRODUCT_NOT_FOUND_WITH_NAME, productName));
        });
    }

    /***
     * This method will return the product entity based on product name.
     * @param criteria
     * @return
     */
    @Override
    public List<ProductResponse> searchProduct(String criteria) {
        List<Product> productList = productRepository.searchProductBasedOnName(criteria);
        logger.info(LOG_RETRIVE_ALL_DETAILS_FROM_DB_BASED_ON_SEARCH);
        return productMapper.toProductResponseList(productList);
    }

    @Override
    public int getProductStock(String prodID) {
        Product product = productRepository.findByProdID(prodID).orElseThrow(() -> {
            return new ProductExceptions("Product not found");
        });
        return product.getStock();
    }

    @Override
    public ProductResponse updateProductStock(String prodID, int newStock) {
        Product product = productRepository.findByProdID(prodID).orElseThrow(() -> {
            throw new ProductExceptions("Product not found");
        });
        try {
            int result = productRepository.updateProductStock(newStock, product.getProdID());
            return getProduct(prodID);
        } catch (Exception exception) {
            throw new ProductExceptions(exception.getMessage());
        }
    }
}
