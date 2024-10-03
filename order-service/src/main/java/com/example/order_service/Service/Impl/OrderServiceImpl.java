package com.example.order_service.Service.Impl;

import com.example.order_service.DTO.Request.OrderRequest;
import com.example.order_service.DTO.Response.OrderResponse;
import com.example.order_service.Mapper.OrderMapper;
import com.example.order_service.Model.Orders;
import com.example.order_service.Repository.OrderItemRepository;
import com.example.order_service.Repository.OrderRepository;
import com.example.order_service.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        Orders orders = orderMapper.toOrders(request);
        orderRepository.save(orders);
        orderItemRepository.saveAll(orders.getOrderItems());
        return orderMapper.toOrderResponse(orders);
    }
}
