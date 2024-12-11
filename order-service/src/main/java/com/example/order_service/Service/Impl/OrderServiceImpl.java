package com.example.order_service.Service.Impl;

import com.example.order_service.DTO.Request.OrderRequest;
import com.example.order_service.DTO.Response.OrderResponse;
import com.example.order_service.Exception.OrderException;
import com.example.order_service.Helpers.OrderHelper;
import com.example.order_service.Mapper.OrderItemMapper;
import com.example.order_service.Mapper.OrderMapper;
import com.example.order_service.Model.OrderStatus;
import com.example.order_service.Model.Orders;
import com.example.order_service.Repository.OrderItemRepository;
import com.example.order_service.Repository.OrderRepository;
import com.example.order_service.Service.MailService;
import com.example.order_service.Service.OrderService;
import com.example.order_service.Service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.example.order_service.Messages.OrderMessages.*;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService, MailService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderHelper orderHelper;

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisServiceImpl redisService;

    private static final String cancelled = "CANCELLED";

    /***
     * This method will place order of the customer.
     * @param request
     * @return
     */
    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        // Generate the order details
        Orders orders = orderHelper.generateOrder(request);
        try {
            // Save the order to database
            orderRepository.save(orders);
        } catch (Exception exception) {
            throw new OrderException(exception.getMessage());
        }
        // Delete all order details from cache
        redisService.deleteData("orderList");

        log.info(ORDER_PLACED, orders.getOrderID());
        return getOrderDetails(orders.getOrderID());
    }

    /***
     * This method will fetch the order based on orderID
     * @param orderID
     * @return
     */
    @Override
    public OrderResponse getOrderDetails(String orderID) {
        // Check if order details are present in cache
        OrderResponse orderResponse = redisService.getData(orderID, OrderResponse.class);
        if (orderResponse != null) {
            return orderResponse;
        } else {
            // If it is not present in cache, load the data from database
            Orders orders = orderRepository.findById(orderID).orElseThrow(() -> {
                return new OrderException(ORDER_NOT_FOUND_WITH_ID + orderID);
            });
            // Convert the order to orderResponse
            orderResponse = orderMapper.toOrderResponse(orders);

            // Save the data to cache
            redisService.setData(orderID, orderResponse, 300L);
            log.info(ORDER_FOUND_WITH_ID, orders.getOrderID());
            return orderResponse;
        }
    }

    /***
     * This method will fetch all orders.
     * @return
     */
    @Override
    public List<OrderResponse> getAllOrders() {
        // Check if all order details are present in cache
        List<OrderResponse> orderResponses = redisService.getData("orderList", List.class);
        if (orderResponses != null) {
            return orderResponses;
        } else {
            // If it is not present in cache, then load the data from database
            List<Orders> ordersList = orderRepository.findAll();
            orderResponses = orderMapper.toOrderResponseList(ordersList);

            // Save the data to cache
            redisService.setData("orderList", orderResponses, 300L);
            return orderResponses;
        }
    }

    /***
     * This method is used to update the order status like Shipped or Delivered etc.
     * @param orderID
     * @param orderStatus
     * @return
     */
    @Override
    public OrderResponse updateOrderStatus(String orderID, String orderStatus) {
        // Convert the string order status to enum
        OrderStatus newOrderStatus = OrderStatus.valueOf(orderStatus);
        try {
            // Update the order status in database
            orderRepository.updateOrderStatus(newOrderStatus, Timestamp.from(Instant.now()), orderID);
            // Delete the existing order info in cache
            redisService.deleteData(orderID);
            // Delete all order data in cache
            redisService.deleteData("orderList");
            log.info(ORDER_STATUS_UPDATED);
            return getOrderDetails(orderID);
        } catch (Exception e) {
            log.error(UNABLE_TO_UPDATE_ORDER_STATUS);
            throw new OrderException("Unable to update order");
        }
    }

    /***
     * This method is used to cancel order by using order ID
     * @param orderID
     * @return
     */
    @Override
    public String cancelOrder(String orderID) {
        // Search if the order ID is present or not
        try {
            Orders orders = orderRepository.findById(orderID).orElseThrow(() -> {
                return new OrderException("Order is not found.");
            });
            // Update the product stock after cancelling the product
            orderHelper.updateProductAfterCancel(orders.getOrderItems());
            // Update the order status in database
            orderRepository.updateOrderStatus(OrderStatus.CANCELLED, Timestamp.from(Instant.now()), orderID);
            // Delete the order data from cache
            redisService.deleteData(orderID);
            // Delete all order data from cache
            redisService.deleteData("orderList");
            log.info(ORDER_CANCELLED_SUCCESSFULLY + orderID);
            return ORDER_CANCELLED_SUCCESSFULLY;
        } catch (Exception e) {
            log.error(UNABLE_TO_CANCEL_ORDER + e.getMessage());
            throw new OrderException(UNABLE_TO_CANCEL_ORDER);
        }
    }

    /***
     * This method is used to get all cancelled orders by username
     * @param username
     * @return
     */
    @Override
    public List<OrderResponse> getAllCancelledOrdersForUser(String username) {
        return getOrdersBasedOnUser(username, cancelled);
    }

    /***
     * This method is used to get the order status based on tracking number
     * @param trackingNumber
     * @return
     */
    @Override
    public OrderResponse getOrderStatusByTrackNumber(String trackingNumber) {
        return orderMapper.toOrderResponse(orderRepository.getOrderByTrackingNumber(trackingNumber));
    }

    /***
     * This method is used to get all orders based on username
     * @param username
     * @return
     */
    @Override
    public List<OrderResponse> getAllOrdersByUserName(String username) {
        return getOrdersBasedOnUser(username, "All");
    }

    /***
     * This method is used to get all orders based on created date
     * @param criteria
     * @return
     */
    @Override
    public List<OrderResponse> searchOrdersByCreatedDate(Timestamp criteria) {
        return orderMapper.toOrderResponseList(orderRepository.searchOrdersByCreatedDate(criteria));
    }

    /**
     * This method is used to count of total orders
     *
     * @return
     */
    @Override
    public int getTotalOrders() {
        // Check if all orders data present in cache
        List<OrderResponse> orderResponses = redisService.getData("orderList", List.class);
        if (orderResponses != null) {
            // If it is present in cache, then return the size
            return orderResponses.size();
        }
        // If it is not present in cache, then load the data from database
        List<Orders> orders = orderRepository.findAll();
        orderResponses = orderMapper.toOrderResponseList(orders);
        // Save the data to cache
        redisService.setData("orderList", orderResponses, 300L);
        return orderResponses.size();
    }

    /**
     * This method is used to get the list of orders based on creation time
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<OrderResponse> getOrderFilter(String start, String end) {
        // Check if all order data is present in cache
        List<OrderResponse> orderResponses = redisService.getData("orderList", List.class);
        if (orderResponses != null) {
            //If it is present, then filter the order based on creation time
            return orderHelper.filterOrders(start, end, orderResponses);
        }
        // If it is not present, then load the data from database
        List<Orders> ordersList = orderRepository.findAll();
        orderResponses = orderMapper.toOrderResponseList(ordersList);
        // Save the order data to cache
        redisService.setData("orderList", orderResponses, 300L);
        return orderHelper.filterOrders(start, end, orderResponses);
    }

    /**
     * This method will get the count of all cancelled orders
     *
     * @return
     */
    @Override
    public int getAllCancelledOrders() {
        // Check if all order data is present in cache
        List<OrderResponse> orderResponses = redisService.getData("orderList", List.class);
        if (orderResponses != null) {
            // If it is present, then filter the order based on cancelled status
            return orderResponses.stream().filter(orderResponse -> orderResponse.getOrderStatus().equals(cancelled)).collect(Collectors.toList()).size();
        }
        // If it is not present, then load the data from database
        List<Orders> ordersList = orderRepository.findAll();
        orderResponses = orderMapper.toOrderResponseList(ordersList);
        // Save the order data to cache
        redisService.setData("orderList", orderResponses, 300L);
        return orderResponses.stream().filter(orderResponse -> orderResponse.getOrderStatus().equals(cancelled)).collect(Collectors.toList()).size();
    }


    /***
     * This method is used to send the mail to user email after placing the order.
     * @param recipient
     * @return
     */
    @Override
    public String sendMail(String recipient) {
        // Sender Mail
        String sender = "bharathrajb26@gmail.com";
        // Using host as local host
        String host = "127.0.0.1";
        // Getting System Properties
        Properties properties = System.getProperties();
        //Setting up email server
        properties.setProperty("mail.smtp.host", host);
        // Create session object to get properties
        Session session = Session.getDefaultInstance(properties);
        try {
            MimeMessage message = new MimeMessage(session);
            // Set From Field: adding senders email to from field.
            message.setFrom(new InternetAddress(sender));
            // Set To Field: adding recipient's email to from field.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            // Set Subject: subject of the email
            message.setSubject("This is Subject");
            // set body of the email.
            message.setText("This is a test mail");
            // Send email.
            Transport.send(message);
            log.info(MAIL_SENT);
            return "Mail Sent";
        } catch (MessagingException e) {
            throw new OrderException(e.getMessage());
        }
    }

    /**
     * This method is used to get the total revenue
     *
     * @return
     */
    @Override
    public double getTotalRevenue() {
        // Check if all order data is present in cache
        List<OrderResponse> orderResponses = redisService.getData("orderList", List.class);
        if (orderResponses != null) {
            // If it is present, then return the total revenue
            return orderResponses.stream().mapToDouble(OrderResponse::getTotalAmount).sum();
        }
        // If it is not present, then load the data from database
        List<Orders> ordersList = orderRepository.findAll();
        orderResponses = orderMapper.toOrderResponseList(ordersList);
        // Save the order data to cache
        redisService.setData("orderList", orderResponses, 300L);
        // Return sum of total amount
        return orderResponses.stream().mapToDouble(OrderResponse::getTotalAmount).sum();
    }

    /**
     * This method will get the total revenue based on the filter
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public double getTotalRevenueFilter(String start, String end) {
        // Check all order data is present in cache
        List<OrderResponse> orderResponses = redisService.getData("orderList", List.class);
        if (orderResponses != null) {
            // If it is present, then filter the order based on the time and return the sum of all orders
            return orderHelper.filterOrders(start, end, orderResponses).stream().mapToDouble(OrderResponse::getTotalAmount).sum();
        }
        // If it is not present, then load the data from database
        List<Orders> ordersList = orderRepository.findAll();
        orderResponses = orderMapper.toOrderResponseList(ordersList);
        // Save the data to cache
        redisService.setData("orderList", orderResponses, 300L);
        // Filter the order based on given time range
        List<OrderResponse> filteredOrders = orderHelper.filterOrders(start, end, orderResponses);
        // return the sum of all orders
        return filteredOrders.stream().mapToDouble(OrderResponse::getTotalAmount).sum();
    }

    /**
     * This method will return list of orders based on the user and operation type
     *
     * @param username
     * @param operationType
     * @return
     */
    private List<OrderResponse> getOrdersBasedOnUser(String username, String operationType) {
        // Key is used to fetch the order from cache
        String key = "orders - " + username;
        // Check order data is present in cache
        List<OrderResponse> orderResponses = redisService.getData(key, List.class);
        if (orderResponses != null) {
            // If it is present, then filter orders based on operation type
            return orderHelper.filterOrderResponses(orderResponses, operationType);
        }
        // If it is not present, then load the data from database
        List<Orders> ordersList = orderRepository.getOrdersByUsername(username);
        orderResponses = orderMapper.toOrderResponseList(ordersList);
        // Save the data to cache
        if (orderResponses != null || !orderResponses.isEmpty()) {
            redisService.setData(key, orderResponses, 300L);
        }
        // Filter the data based on operation type
        return orderHelper.filterOrderResponses(orderResponses, operationType);
    }

}
