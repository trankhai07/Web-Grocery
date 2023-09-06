package com.web_food.library.service;

import com.web_food.library.dto.CategoryDto;
import com.web_food.library.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category save(Category category);
    Category update(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAllActivatedTrue();
    List<Category> findAll();
    void deleteById(Long id);
    void enableById(Long id);
    List<CategoryDto> getCategorySize();
    Category findCategoryById(Long id);
}
