package com.example.product_service.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="category")
public class Category {
    @Id
    @Column(name = "category_id")
    private String categoryID;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_description")
    private String categoryDescription;

    @Column(name = "created_at")
    private Timestamp created_at;

    @Column(name = "updated_at")
    private Timestamp updated_at;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "meta_title")
    private String meta_title;

    @Column(name = "meta_description")
    private String meta_description;

    @Column(name = "meta_keywords")
    private String meta_keywords;
}
