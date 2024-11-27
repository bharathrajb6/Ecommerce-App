package com.example.order_service.Controller;

import com.example.order_service.DTO.Request.OrderRequest;
import com.example.order_service.DTO.Response.OrderResponse;
import com.example.order_service.Service.OrderService;
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
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public OrderResponse placeOrder(@RequestBody OrderRequest request) {
        return orderService.placeOrder(request);
    }

    /***
     * This method is used to get order details by orderID.
     * @param orderID
     * @return
     */
//    @RequestMapping(value = "/order/{orderID}", method = RequestMethod.GET)
    public OrderResponse getOrderDetails(@PathVariable String orderID) {
        return orderService.getOrderDetails(orderID);
    }

    /***
     * This method is used to get all orders.
     * @return
     */
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
    @RequestMapping(value = "/order/{orderID}/status", method = RequestMethod.PUT)
    public OrderResponse updateOrderStatus(@PathVariable String orderID, @RequestBody String orderStatus) {
        return orderService.updateOrderStatus(orderID, orderStatus);
    }

    /***
     * This method is used to cancel order.
     * @param orderID
     * @return
     */
    @RequestMapping(value = "/order/{orderID}/cancel", method = RequestMethod.PUT)
    public String cancelOrder(@PathVariable String orderID) {
        return orderService.cancelOrder(orderID);
    }

    /***
     * This method is used to get all cancelled orders by username.
     * @param username
     * @return
     */
    @RequestMapping(value = "/order/{username}/cancelled", method = RequestMethod.GET)
    public List<OrderResponse> getAllCancelledOrdersForUser(@PathVariable String username) {
        return orderService.getAllCancelledOrdersForUser(username);
    }

    /***
     * This method is used to get order status by tracking number.
     * @param trackingNumber
     * @return
     */
    @RequestMapping(value = "/order/track/{trackingNumber}", method = RequestMethod.GET)
    public OrderResponse getOrderByTrackingNumber(@PathVariable String trackingNumber) {
        return orderService.getOrderStatusByTrackNumber(trackingNumber);
    }

    /***
     * This method is used to search orders by created date.
     * @param criteria
     * @return
     */
    @RequestMapping(value = "/order/search/{username}", method = RequestMethod.GET)
    public List<OrderResponse> getAllOrdersByUserName(@PathVariable String username) {
        return orderService.getAllOrdersByUserName(username);
    }

    @RequestMapping(value = "/order/totalOrders", method = RequestMethod.GET)
    public int getTotalOrders() {
        return orderService.getTotalOrders();
    }

    @RequestMapping(value = "/order/filter", method = RequestMethod.GET)
    public List<OrderResponse> getOrdersFilter(@RequestParam("start") String start, @RequestParam("end") String end) {
        return orderService.getOrderFilter(start, end);
    }

    @RequestMapping(value = "/order/cancelled", method = RequestMethod.GET)
    public int getAllCancelledOrders() {
        return orderService.getAllCancelledOrders();
    }

    @RequestMapping(value = "/order/revenue", method = RequestMethod.GET)
    public double getTotalRevenue() {
        return orderService.getTotalRevenue();
    }

    @RequestMapping(value = "/order/revenue/filter", method = RequestMethod.GET)
    public double getTotalRevenueFilter(@RequestParam("start") String start, @RequestParam("end") String end) {
        return orderService.getTotalRevenueFilter(start, end);
    }
}
