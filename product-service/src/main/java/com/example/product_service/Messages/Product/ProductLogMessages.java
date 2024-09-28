package com.example.product_service.Messages.Product;

public class ProductLogMessages {
    public static final String LOG_PRODUCT_ALREADY_EXISTS = "Product {} already exists.";
    public static final String LOG_PRODUCT_SAVED_SUCCESSFULLY = "Product {} (ID: {}) has been saved successfully.";
    public static final String LOG_PRODUCT_DELETED_SUCCESSFULLY = "Product {} (ID: {}) has been deleted successfully.";
    public static final String LOG_UNABLE_TO_SAVE_PRODUCT = "Unable to save the product {}. Error - {}";
    public static final String LOG_PRODUCT_FETCHED_FROM_DB = "Product {} has been retrived from database.";
    public static final String LOG_PRODUCT_NOT_FOUND_WITH_NAME = "Product not found with name: {}";
    public static final String LOG_PRODUCT_NOT_FOUND_WITH_ID = "Product not found with id: {}";
    public static final String LOG_PRODUCT_STOCK_UPDATED_SUCCESSFULLY = "Product {} (ID - {}) stock has been updated successfully.";
    public static final String LOG_UNABLE_TO_DELETE_PRODUCT = "Unable to delete the product with name {} (ID-{}).";
    public static final String LOG_RETRIVE_ALL_DETAILS_FROM_DB_BASED_ON_SEARCH = "Retrive all product information based on search criteria - %s";
}
