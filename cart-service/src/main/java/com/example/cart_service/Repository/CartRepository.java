package com.example.cart_service.Repository;

import com.example.cart_service.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    /**
     * This method will return the cart based on username
     *
     * @param username
     * @return
     */
    Optional<Cart> findByUsername(String username);

    /**
     * This method will update the total amount of the cart
     *
     * @param totalAmount
     * @param cartID
     */
    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.totalAmount = ?1 where c.cartID = ?2")
    void updateTotalAmount(double totalAmount, String cartID);

    /**
     * This method will update the last updated time of the cart
     *
     * @param timestamp
     * @param username
     */
    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.updatedAt = ?1 where c.username = ?2")
    void updateByLastUpdateTime(Timestamp timestamp, String username);
}
