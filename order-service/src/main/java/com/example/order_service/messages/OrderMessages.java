package com.example.order_service.messages;

public class OrderMessages {

    public static final String ORDER_CREATED = "Order has been created successfully.";
    public static final String ORDER_PLACED = "Order has been placed successfully.Order ID - {}";
    public static final String PRODUCT_UPDATED_AFTER_ORDER_CANCEL = "Product {} has been updated successfully after cancelling the order.";
    public static final String UNABLE_UPDATE_PRODUCT_AFTER_ORDER_CANCEL = "Unable to update the product {} after order cancellation";
    public static final String ORDER_NOT_FOUND_WITH_ID = "Order is not found with ID - ";
    public static final String ORDER_FOUND_WITH_ID = "Order is found with ID - {}";
    public static final String ORDER_STATUS_UPDATED = "Order status has been updated successfully";
    public static final String UNABLE_TO_UPDATE_ORDER_STATUS = "Unable to update the order status.";
    public static final String ORDER_CANCELLED_SUCCESSFULLY = "Order has been cancelled successfully ";
    public static final String UNABLE_TO_CANCEL_ORDER = "Unable to cancel the order";
    public static final String MAIL_SENT = "Mail has been sent successfully.";
}
