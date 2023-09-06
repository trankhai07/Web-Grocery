package com.web_food.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products",uniqueConstraints = @UniqueConstraint(columnNames = {"name", "image"}))
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;
    private String description;
    private int currentQuantity;
    private Double cost;
    private Double sale;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id",referencedColumnName = "category_id")
    private Category category;

    private Boolean is_deleted;
    private Boolean is_activated;

}
