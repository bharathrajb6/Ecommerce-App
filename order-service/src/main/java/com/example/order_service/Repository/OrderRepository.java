package com.example.order_service.Repository;


import com.example.order_service.Model.OrderStatus;
import com.example.order_service.Model.Orders;
import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {

    @Modifying
    @Transactional
    @Query("UPDATE Orders o SET o.orderStatus = ?1,o.updatedAt = ?2 WHERE o.orderID = ?3")
    void updateOrderStatus(OrderStatus status, Timestamp updatedAt, String orderID);


    @Query("SELECT o from Orders o where o.orderStatus = ?1 and o.username = ?2")
    List<Orders> getAllCancelledOrders(OrderStatus orderStatus, String username);

    @Query("SELECT o from Orders o where o.trackingNumber = ?1")
    Orders getOrderByTrackingNumber(String trackingNumber);

    List<Orders> getOrdersByUsername(String username);

    @Query("SELECT o FROM Orders o WHERE o.createdAt = ?1")
    List<Orders> searchOrdersByCreatedDate(Timestamp createdAt);


    @Query("SELECT o from Orders o where o.orderStatus LIKE CONCAT('%', ?1, '%')")
    List<Orders> searchOrdersByOrderStatus(String status);

}
