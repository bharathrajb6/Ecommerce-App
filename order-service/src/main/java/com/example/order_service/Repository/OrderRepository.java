package com.example.order_service.Repository;


import com.example.order_service.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository  extends JpaRepository<Orders,String> {

}
