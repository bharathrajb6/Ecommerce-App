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
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.example.order_service.messages.OrderMessages.*;

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

    /***
     * This method will place order of the customer.
     * @param request
     * @return
     */
    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        Orders orders = orderHelper.generateOrder(request);
        orderRepository.save(orders);
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
        Orders orders = orderRepository.findById(orderID).orElseThrow(() -> {
            return new OrderException(ORDER_NOT_FOUND_WITH_ID + orderID);
        });
        log.info(ORDER_FOUND_WITH_ID, orders.getOrderID());
        return orderMapper.toOrderResponse(orders);
    }

    /***
     * This method will fetch all orders.
     * @return
     */
    @Override
    public List<OrderResponse> getAllOrders() {
        List<Orders> ordersList = orderRepository.findAll();
        return orderMapper.toOrderResponseList(ordersList);
    }

    /***
     * This method is used to update the product status
     * @param orderID
     * @param orderStatus
     * @return
     */
    @Override
    public OrderResponse updateOrderStatus(String orderID, String orderStatus) {
        OrderStatus newOrderStatus = OrderStatus.valueOf(orderStatus);
        try {
            orderRepository.updateOrderStatus(newOrderStatus, Timestamp.from(Instant.now()), orderID);
            log.info(ORDER_STATUS_UPDATED);
            return getOrderDetails(orderID);
        } catch (Exception e) {
            log.error(UNABLE_TO_UPDATE_ORDER_STATUS);
            throw new OrderException("Unable to update order");
        }
    }

    /***
     * This method is used to cancel order
     * @param orderID
     * @return
     */
    @Override
    public String cancelOrder(String orderID) {
        try {
            Orders orders = orderRepository.findById(orderID).orElseThrow(() -> {
                return new OrderException("Order is not found.");
            });
            orderHelper.updateProductAfterCancel(orders.getOrderItems());
            orderRepository.updateOrderStatus(OrderStatus.CANCELLED, Timestamp.from(Instant.now()), orderID);
            log.info(ORDER_CANCELLED_SUCCESSFULLY + orderID);
            return ORDER_CANCELLED_SUCCESSFULLY;
        } catch (Exception e) {
            log.error(UNABLE_TO_CANCEL_ORDER + e.getMessage());
            throw new OrderException(UNABLE_TO_CANCEL_ORDER);
        }
    }

    /***
     * This method is used to get all cancelled orders
     * @param username
     * @return
     */
    @Override
    public List<OrderResponse> getAllCancelledOrders(String username) {
        List<Orders> ordersList = orderRepository.getAllCancelledOrders(OrderStatus.CANCELLED, username);
        return orderMapper.toOrderResponseList(ordersList);
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
        return orderMapper.toOrderResponseList(orderRepository.getOrdersByUsername(username));
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
}
