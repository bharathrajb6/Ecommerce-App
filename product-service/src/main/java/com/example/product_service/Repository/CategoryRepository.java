package com.example.product_service.Repository;

import com.example.product_service.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    public Optional<Category> findByCategoryID(String categoryID);

    public Optional<Category> findByCategoryName(String categoryName);

    @Transactional
    public void deleteByCategoryID(String categoryID);

    @Transactional
    public void deleteByCategoryName(String categoryName);

    @Transactional
    public void deleteAll();

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.categoryName = ?1, c.categoryDescription = ?2,c.updated_at = ?3 WHERE c.categoryID = ?4")
    void updateCategoryByID(String categoryName, String categoryDescription, Timestamp updatedAt, String categoryID);

    boolean existsByCategoryName(String categoryName);
}
