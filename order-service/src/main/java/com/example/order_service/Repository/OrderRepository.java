package com.example.order_service.Repository;


import com.example.order_service.Model.OrderStatus;
import com.example.order_service.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {

    /***
     * This method is used to update order status by order id
     * @param status
     * @param updatedAt
     * @param orderID
     */
    @Modifying
    @Transactional
    @Query("UPDATE Orders o SET o.orderStatus = ?1,o.updatedAt = ?2 WHERE o.orderID = ?3")
    void updateOrderStatus(OrderStatus status, Timestamp updatedAt, String orderID);

    /***
     * This method is used to get order details by tracking number
     * @param trackingNumber
     * @return
     */
    @Query("SELECT o from Orders o where o.trackingNumber = ?1")
    Orders getOrderByTrackingNumber(String trackingNumber);

    /***
     * This method is used to get all orders by username
     * @param username
     * @return
     */
    List<Orders> getOrdersByUsername(String username);

    /***
     * This method is used to search orders by created date
     * @param createdAt
     * @return
     */
    @Query("SELECT o FROM Orders o WHERE o.createdAt = ?1")
    List<Orders> searchOrdersByCreatedDate(Timestamp createdAt);
}
