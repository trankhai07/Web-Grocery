package com.web_food.library.dto;

import com.web_food.library.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private int currentQuantity;
    private Double cost;
    private Double sale;
    private String image;
    private Category category;
    private Boolean is_deleted;
    private Boolean is_activated;
}
