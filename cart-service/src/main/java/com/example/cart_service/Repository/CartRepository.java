package com.example.cart_service.Repository;

import com.example.cart_service.Model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartItems,String> {

}
