package com.example.cart_service.Repository;

import com.example.cart_service.Model.Cart;
import com.example.cart_service.Model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems,String> {

    @Modifying
    @Transactional
    @Query("DELETE CartItems c where c.cart = ?1 and c.prodID = ?2")
    void deleteProductFromCart(Cart cart, String prodID);

    @Modifying
    @Transactional
    void deleteByCart(Cart cart);
}
