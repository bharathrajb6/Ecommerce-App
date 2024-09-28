package com.example.product_service.Repository;

import com.example.product_service.Model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {

    public Optional<Brand> findByBrandName(String brandName);

    public Optional<Brand> findByBrandID(String brandID);

    @Transactional
    public void deleteByBrandName(String brandName);

    @Transactional
    public void deleteByBrandID(String brandID);

    @Modifying
    @Transactional
    @Query("UPDATE Brand b SET b.brandName = ?1, b.brandDescription = ?2, b.updatedDate = ?3 WHERE b.brandID = ?4")
    int updateBrandByID(String name, String description, Timestamp updatedAt, String brandID);

    @Modifying
    @Transactional
    @Query("UPDATE Brand b SET b.brandDescription = ?1, b.updatedDate = ?2 WHERE b.brandName = ?3")
    int updateBrandByName(String description, Timestamp updatedAt, String name);

    boolean existsByBrandName(String brandName);
}
