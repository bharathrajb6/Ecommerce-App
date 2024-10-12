package com.example.order_service.Repository;

import com.example.order_service.Model.OrderItems;
import com.example.order_service.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems,String> {

    /***
     * Find all order items for a given order
     * @param order
     * @return
     */
    @Query("select o from OrderItems o where o.order = ?1")
    List<OrderItems> findAllOrderItems(Orders order);
}
