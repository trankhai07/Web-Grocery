package com.web_food.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discounts", uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Long id;

    private String code;
    private double CostSale;
    private boolean dis_active;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discount")
    private List<Order> Order;
}
