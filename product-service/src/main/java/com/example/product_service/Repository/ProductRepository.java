package com.example.product_service.Repository;

import com.example.product_service.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findByProdName(String prodName);

    Optional<Product> findByProdID(String prodID);

    List<Product> findByCategory(String category);

    List<Product> findByBrand(String brandID);

    @Transactional
    void deleteByProdID(String prodID);

    @Transactional
    void deleteByProdName(String prodName);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.prodDescription = ?1,p.price = ?2, p.discountPrice = ?3, p.stock = ?4, p.updated_at = ?5 where p.prodID = ?6")
    int updateProductByID(String description, int price, int discountPrice, int stock, Timestamp updated_at, String prodID);

    boolean existsByProdName(String productName);

    @Query("SELECT p FROM Product p WHERE p.prodName LIKE CONCAT('%', ?1, '%')")
    List<Product> searchProductBasedOnName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stock = ?1 where p.prodID = ?2")
    int updateProductStock(int newStock, String prodID);
}
