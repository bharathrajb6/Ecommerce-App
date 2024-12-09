package com.example.product_service.Service.Impl;

import com.example.product_service.DTO.Request.Product.ProductCategoryRequest;
import com.example.product_service.DTO.Request.Product.ProductRequest;
import com.example.product_service.DTO.Response.CategoryResponse;
import com.example.product_service.DTO.Response.Product.ProductResponse;
import com.example.product_service.Exceptions.ProductExceptions;
import com.example.product_service.Helper.ProductHelper;
import com.example.product_service.Model.Product;
import com.example.product_service.Repository.BrandRepository;
import com.example.product_service.Repository.CategoryRepository;
import com.example.product_service.Repository.ProductRepository;
import com.example.product_service.Service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.product_service.Messages.Product.ProductExceptionMessages.*;
import static com.example.product_service.Messages.Product.ProductLogMessages.*;
import static com.example.product_service.Utils.CommonUtils.UUIDChecker;
import static com.example.product_service.validations.ProductValidationHandler.validateProductDetails;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    private ProductHelper productHelper;

    /***
     * This method is used to add the product.
     * @param request
     * @return
     */
    @Override
    @Transactional
    public ProductResponse addProduct(ProductRequest request) {

        // Check if product name is already present in database
        if (productRepository.existsByProdName(request.getProdName())) {
            log.error(LOG_PRODUCT_ALREADY_EXISTS, request.getProdName());
            throw new ProductExceptions(String.format(EXCEPTION_PRODUCT_ALREADY_FOUND, request.getProdName()));
        }

        Product product = productHelper.toProduct(request);
        // Validate product
        validateProductDetails(product);

        try {

            Timestamp currentTimestamp = Timestamp.from(Instant.now());
            product.setCreated_at(currentTimestamp);
            product.setUpdated_at(currentTimestamp);

            // Save the data to database
            productRepository.save(product);
            redisService.deleteData("productList");
            log.info(LOG_PRODUCT_SAVED_SUCCESSFULLY, product.getProdName(), product.getProdID());
            return getProduct(product.getProdID());
        } catch (Exception e) {
            // Throw product exception if any issue occurred
            log.error(LOG_UNABLE_TO_SAVE_PRODUCT, product.getProdName(), e.getMessage());
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
        // Check if the value is UUID
        if (UUIDChecker(value)) {
            // Get the data from redis cache
            ProductResponse productResponse = redisService.getData(value, ProductResponse.class);
            if (productResponse != null) {
                return productResponse;
            } else {
                // If it's not present in cache, then return it from database
                return productRepository.findByProdID(value).map(product -> {
                    // add the product information to cache
                    ProductResponse response = productHelper.toProductResponse(product);
                    redisService.setData(response.getProdID(), response, 300L);
                    return response;
                }).orElseThrow(() -> {
                    // Throw product exception if any issue occurred
                    log.error(LOG_PRODUCT_NOT_FOUND_WITH_ID, value);
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
        // Get the data from redis cache
        ProductResponse productResponse = redisService.getData(prodName, ProductResponse.class);
        if (productResponse != null) {
            return productResponse;
        } else {
            // If it's not present in cache, then return it from database
            return productRepository.findByProdName(prodName).map(product -> {

                ProductResponse response = productHelper.toProductResponse(product);
                // add the product information to cache
                redisService.setData(response.getProdName(), response, 300L);
                return response;
            }).orElseThrow(() -> {
                // Throw product exception if any issue occurred
                log.error(LOG_PRODUCT_NOT_FOUND_WITH_NAME, prodName);
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
        List<ProductResponse> productResponses = redisService.getData("productList", List.class);
        if (productResponses != null) {
            return productResponses;
        } else {
            List<ProductResponse> productList = productRepository.findAll().stream().map(product -> productHelper.toProductResponse(product)).collect(Collectors.toList());
            redisService.setData("productList", productList, 300L);
            return productList;
        }
    }

    /**
     * This method will update the product stock based on product ID.
     *
     * @param prodID
     * @param request
     * @return
     */
    @Override
    @Transactional
    public ProductResponse updateProduct(String prodID, ProductRequest request) {
        Product product = productRepository.findByProdID(prodID).orElseThrow(() -> {
            log.error(LOG_PRODUCT_NOT_FOUND_WITH_ID, prodID);
            return new ProductExceptions(String.format(EXCEPTION_PRODUCT_NOT_FOUND_WITH_ID, prodID));
        });

        String newDimension = request.getDimensions();
        int newPrice = request.getPrice();
        int newDiscountPrice = request.getDiscountPrice();
        int newStock = request.getStock();

        // Update the product stock by product ID
        productRepository.updateProductByID(newDimension, newPrice, newDiscountPrice, newStock, Timestamp.from(Instant.now()), prodID);

        log.info(LOG_PRODUCT_STOCK_UPDATED_SUCCESSFULLY, product.getProdName(), product.getProdID());
        // Delete the product data in cache
        redisService.deleteData(prodID);
        redisService.deleteData(product.getProdName());
        redisService.deleteData("productList");
        return getProduct(prodID);
    }

    /***
     * Deletes the product entity based on product ID.
     * @param value
     * @return
     */
    @Override
    @Transactional
    public String deleteProduct(String value) {
        // Check if the value is UUID
        if (UUIDChecker(value)) {
            return productRepository.findByProdID(value).map(product -> {
                try {
                    // Delete the product information from database
                    productRepository.delete(product);

                    // Delete the product information from cache
                    redisService.deleteData(value);
                    redisService.deleteData("productList");

                    log.info(LOG_PRODUCT_DELETED_SUCCESSFULLY, product.getProdName(), product.getProdID());
                    return PRODUCT_DELETED_SUCCESSFULLY;
                } catch (Exception e) {
                    // If any issues occures throw Product Exception
                    log.error(LOG_UNABLE_TO_DELETE_PRODUCT, product.getProdName(), product.getProdID());
                    throw new ProductExceptions(String.format(EXCEPTION_UNABLE_TO_DELETE_PRODUCT, product.getProdName()));
                }
            }).orElseThrow(() -> {
                // If product is not found then throw product exception
                log.error(LOG_PRODUCT_NOT_FOUND_WITH_ID, value);
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
                // Delete the product information from database
                productRepository.delete(product);

                // Delete the product information from cache
                redisService.deleteData(productName);
                redisService.deleteData("productList");

                log.info(LOG_PRODUCT_DELETED_SUCCESSFULLY, product.getProdName(), product.getProdID());
                return PRODUCT_DELETED_SUCCESSFULLY;
            } catch (Exception e) {
                // If any issues occures throw Product Exception
                log.error(LOG_UNABLE_TO_DELETE_PRODUCT, product.getProdName(), product.getProdID());
                throw new ProductExceptions(String.format(EXCEPTION_UNABLE_TO_DELETE_PRODUCT, product.getProdName()));
            }
        }).orElseThrow(() -> {
            // If product is not found then throw product exception
            log.error(LOG_PRODUCT_NOT_FOUND_WITH_NAME, productName);
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
        log.info(LOG_RETRIVE_ALL_DETAILS_FROM_DB_BASED_ON_SEARCH);
        return productList.stream().map(product -> productHelper.toProductResponse(product)).collect(Collectors.toList());
    }

    /**
     * This method will return current stock of the product
     *
     * @param prodID
     * @return
     */
    @Override
    public int getProductStock(String prodID) {
        ProductResponse productResponse = redisService.getData(prodID, ProductResponse.class);
        if (productResponse != null) {
            return productResponse.getStock();
        } else {
            Product product = productRepository.findByProdID(prodID).orElseThrow(() -> {
                return new ProductExceptions(String.format(EXCEPTION_PRODUCT_NOT_FOUND_WITH_ID, prodID));
            });
            return product.getStock();
        }
    }

    /**
     * This method will update the stock of the product
     *
     * @param prodID
     * @param newStock
     * @return
     */
    @Override
    @Transactional
    public ProductResponse updateProductStock(String prodID, int newStock) {
        Product product = productRepository.findByProdID(prodID).orElseThrow(() -> {
            throw new ProductExceptions(String.format(EXCEPTION_PRODUCT_NOT_FOUND_WITH_ID, prodID));
        });
        try {
            // Update the product stock
            productRepository.updateProductStock(newStock, product.getProdID());
            // Update the product data in cache
            redisService.deleteData(prodID);
            redisService.deleteData(product.getProdName());
            redisService.deleteData("productList");
            return getProduct(prodID);
        } catch (Exception exception) {
            // If any issue occured then throw product exception with message
            throw new ProductExceptions(exception.getMessage());
        }
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String categoryID) {
        String key = "ProductCategory - " + categoryID;
        List<ProductResponse> productResponses = redisService.getData(key, List.class);
        if (productResponses != null) {
            return productResponses;
        } else {
            String existingCategoryID = productHelper.getCategoryDetails(categoryID).getId();
            List<Product> productList = productRepository.getProductByCategory(existingCategoryID);
            productResponses = productList.stream().map(product -> productHelper.toProductResponse(product)).collect(Collectors.toList());
            redisService.setData(key, productResponses, 300L);
            return productResponses;
        }
    }
}
