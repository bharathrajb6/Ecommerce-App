package com.example.product_service.Controller;

import com.example.product_service.DTO.Request.Product.ProductRequest;
import com.example.product_service.DTO.Response.Product.ProductResponse;
import com.example.product_service.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    ProductService productService;

    /***
     * This method is responsible for handling POST requests to add a new product.
     * @param request
     * @return
     */
    @Operation(summary = "Add Product", description = "Adds a new product to product catalog")
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ProductResponse addProduct(@RequestBody ProductRequest request) {
        return productService.addProduct(request);
    }

    /***
     * This method is responsible for handling GET requests to get a product by its ID.
     * @param productID
     * @return
     */
    @Operation(summary = "Get Product", description = "Get the product from catalog based on product ID")
    @RequestMapping(value = "/product/{productID}", method = RequestMethod.GET)
    public ProductResponse getProducts(@PathVariable String productID) {
        return productService.getProduct(productID);
    }

    /***
     * This method is responsible for handling GET requests to get all products.
     * @return
     */
    @Operation(summary = "Get All Products", description = "Get all products from catalog")
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * This method is responsible for handling PUT requests to update a product.
     * @param productID
     * @param request
     * @return
     */
    @Operation(summary = "Update Product", description = "Update the product in catalog")
    @RequestMapping(value = "/product/{productID}", method = RequestMethod.PUT)
    public ProductResponse updateProduct(@PathVariable String productID, @RequestBody ProductRequest request) {
        return productService.updateProduct(productID, request);
    }

    /***
     * This method is responsible for handling DELETE requests to delete a product.
     * @param productID
     * @return
     */
    @Operation(summary = "Delete Product", description = "Delete the product from catalog")
    @RequestMapping(value = "/product/{productID}", method = RequestMethod.DELETE)
    public String deleteProduct(@PathVariable String productID) {
        return productService.deleteProduct(productID);
    }

    /***
     * This method is responsible for handling GET requests to get all products by category.
     * @param criteria
     * @return
     */
    @Operation(summary = "Search Product", description = "Search product in catalog")
    @RequestMapping(value = "/product/search/{criteria}", method = RequestMethod.GET)
    public List<ProductResponse> searchProduct(@PathVariable String criteria) {
        return productService.searchProduct(criteria);
    }

    @Operation(summary = "Get Product Stock", description = "Get the product stock from catalog")
    @RequestMapping(value = "/product/{productID}/stock", method = RequestMethod.GET)
    public int getStock(@PathVariable String productID) {
        return productService.getProductStock(productID);
    }


    @Operation(summary = "Update Product Stock", description = "Update the product stock in catalog")
    @RequestMapping(value = "/product/{productID}/stock", method = RequestMethod.PUT)
    public ProductResponse updateProductStock(@PathVariable String productID, @RequestBody int newStock) {
        return productService.updateProductStock(productID, newStock);
    }
}
