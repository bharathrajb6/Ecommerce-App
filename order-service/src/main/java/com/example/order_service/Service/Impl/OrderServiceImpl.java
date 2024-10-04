package com.example.order_service.Service.Impl;

import com.example.order_service.DTO.Request.OrderRequest;
import com.example.order_service.DTO.Response.OrderItemResponse;
import com.example.order_service.DTO.Response.OrderResponse;
import com.example.order_service.Helpers.OrderHelper;
import com.example.order_service.Mapper.OrderItemMapper;
import com.example.order_service.Mapper.OrderMapper;
import com.example.order_service.Model.Orders;
import com.example.order_service.Repository.OrderItemRepository;
import com.example.order_service.Repository.OrderRepository;
import com.example.order_service.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
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

    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        Orders orders = orderHelper.generateOrder(request);
        orderRepository.save(orders);
        return getOrderDetails(orders.getOrderID());
    }

    @Override
    public OrderResponse getOrderDetails(String orderID) {
        Orders orders = orderRepository.findById(orderID).orElse(null);
        OrderResponse orderResponse = orderMapper.toOrderResponse(orders);
        List<OrderItemResponse> orderItemResponse = orderItemMapper.toOrderItemResponse(orders.getOrderItems());
        orderResponse.setOrderItems(orderItemResponse);
        return orderResponse;
    }
}
