package com.example.product_service.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Brand")
public class Brand {
    @Id
    @Column(name = "brand_id")
    private String brandID;

    @Column(name = "name")
    private String brandName;

    @Column(name = "description")
    private String brandDescription;

    @Column(name = "created_at")
    private Timestamp createdDate;

    @Column(name = "updated_at")
    private Timestamp updatedDate;
}
