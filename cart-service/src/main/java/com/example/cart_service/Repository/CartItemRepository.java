package com.example.cart_service.Repository;

import com.example.cart_service.Model.Cart;
import com.example.cart_service.Model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, String> {

    /**
     * This method will delete the product from cart items
     *
     * @param cart
     * @param prodID
     */
    @Modifying
    @Transactional
    @Query("DELETE CartItems c where c.cart = ?1 and c.prodID = ?2")
    void deleteProductFromCart(Cart cart, String prodID);

    /**
     * This method will delete all products for specific cart
     *
     * @param cart
     */
    @Modifying
    @Transactional
    void deleteByCart(Cart cart);

    /**
     * This method will the cart items based on cart
     *
     * @param cart
     * @return
     */
    List<CartItems> findByCart(Cart cart);

    /**
     * This method will return the cart item based on cart and productID.
     *
     * @param cart
     * @param prodID
     * @return
     */
    @Query("SELECT Cart c where c.cart = ?1,c.prodID = ?2")
    CartItems findByProductIDAndCart(Cart cart, String prodID);


    /**
     * This method update the product quantity and total price for existing product in the cart
     *
     * @param quantity
     * @param totalPrice
     * @param cartItemId
     */
    @Modifying
    @Transactional
    @Query("UPDATE CartItems c SET c.quantity = ?1,c.totalPrice = ?2 where c.cartItemID = cartItemId")
    void updateCartProductQuantity(int quantity, double totalPrice, String cartItemId);
}
