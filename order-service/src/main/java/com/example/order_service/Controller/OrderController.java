package com.example.order_service.Controller;

import com.example.order_service.DTO.Request.OrderRequest;
import com.example.order_service.DTO.Response.OrderResponse;
import com.example.order_service.Service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /***
     * This method is used to place order.
     * @param request
     * @return
     */
    @Operation(summary = "Place Order", description = "Place an order")
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public OrderResponse placeOrder(@RequestBody OrderRequest request) {
        return orderService.placeOrder(request);
    }

    /***
     * This method is used to get order details by orderID.
     * @param orderID
     * @return
     */
    @Operation(summary = "Get Order Details", description = "Get order details based on order ID")
    @RequestMapping(value = "/order/{orderID}", method = RequestMethod.GET)
    public OrderResponse getOrderDetails(@PathVariable String orderID) {
        return orderService.getOrderDetails(orderID);
    }

    /***
     * This method is used to get all orders.
     * @return
     */
    @Operation(summary = "Get All Orders", description = "Get All Orders")
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    /***
     * This method is used to update order status like shipped, delivered etc.
     * @param orderID
     * @param orderStatus
     * @return
     */
    @Operation(summary = "Update Order Status", description = "Update the order status such as PENDING, CANCELLED etc.")
    @RequestMapping(value = "/order/{orderID}/status", method = RequestMethod.PUT)
    public OrderResponse updateOrderStatus(@PathVariable String orderID, @RequestBody String orderStatus) {
        return orderService.updateOrderStatus(orderID, orderStatus);
    }

    /***
     * This method is used to cancel order.
     * @param orderID
     * @return
     */
    @Operation(summary = "Cancel Order", description = "Cancel the order based on order ID")
    @RequestMapping(value = "/order/{orderID}/cancel", method = RequestMethod.PUT)
    public String cancelOrder(@PathVariable String orderID) {
        return orderService.cancelOrder(orderID);
    }

    /***
     * This method is used to get all cancelled orders by username.
     * @param username
     * @return
     */
    @Operation(summary = "Get All Cancelled Orders", description = "Get all cancelled orders for specific username")
    @RequestMapping(value = "/order/{username}/cancelled", method = RequestMethod.GET)
    public List<OrderResponse> getAllCancelledOrdersForUser(@PathVariable String username) {
        return orderService.getAllCancelledOrdersForUser(username);
    }

    /***
     * This method is used to get order status by tracking number.
     * @param trackingNumber
     * @return
     */
    @Operation(summary = "Track Order", description = "Track the order based on tracking number")
    @RequestMapping(value = "/order/track/{trackingNumber}", method = RequestMethod.GET)
    public OrderResponse getOrderByTrackingNumber(@PathVariable String trackingNumber) {
        return orderService.getOrderStatusByTrackNumber(trackingNumber);
    }

    /***
     * This method is used to search orders by created date.
     * @param criteria
     * @return
     */
    @Operation(summary = "Search Order", description = "Search order based on username")
    @RequestMapping(value = "/order/search/{username}", method = RequestMethod.GET)
    public List<OrderResponse> getAllOrdersByUserName(@PathVariable String username) {
        return orderService.getAllOrdersByUserName(username);
    }

    @Operation(summary = "Get Total Orders", description = "Get Total Orders")
    @RequestMapping(value = "/order/totalOrders", method = RequestMethod.GET)
    public int getTotalOrders() {
        return orderService.getTotalOrders();
    }

    @Operation(summary = "Get Orders", description = "Get all orders based on start and end date")
    @RequestMapping(value = "/order/filter", method = RequestMethod.GET)
    public List<OrderResponse> getOrdersFilter(@RequestParam("start") String start, @RequestParam("end") String end) {
        return orderService.getOrderFilter(start, end);
    }

    @Operation(summary = "Get Total Cancelled Orders", description = "Get total cancelled orders")
    @RequestMapping(value = "/order/cancelled", method = RequestMethod.GET)
    public int getAllCancelledOrders() {
        return orderService.getAllCancelledOrders();
    }

    @Operation(summary = "Get Total Revenue", description = "Get total revenue")
    @RequestMapping(value = "/order/revenue", method = RequestMethod.GET)
    public double getTotalRevenue() {
        return orderService.getTotalRevenue();
    }

    @Operation(summary = "Get Revenue", description = "Get revenue based on start date and end date")
    @RequestMapping(value = "/order/revenue/filter", method = RequestMethod.GET)
    public double getTotalRevenueFilter(@RequestParam("start") String start, @RequestParam("end") String end) {
        return orderService.getTotalRevenueFilter(start, end);
    }
}
