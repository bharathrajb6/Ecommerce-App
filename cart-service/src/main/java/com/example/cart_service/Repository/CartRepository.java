package com.example.cart_service.Repository;

import com.example.cart_service.Model.Cart;
import com.example.cart_service.Model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,String> {

    Optional<Cart> findByUsername(String username);
}
