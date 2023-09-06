package com.web_food.library.service.impl;

import com.web_food.library.dto.CategoryDto;
import com.web_food.library.model.Category;
import com.web_food.library.repository.CategoryRepository;
import com.web_food.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        Category category1 = new Category(category.getName());
        return categoryRepository.save(category1);
    }

    @Override
    public Category update(Category category) {
        Optional<Category> category1 = categoryRepository.findById(category.getId());
        category1.get().setName(category.getName());
        return categoryRepository.save(category1.get());
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> findAllActivatedTrue() {
        return categoryRepository.findAllActivatedTrue();
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Optional<Category> category1 = categoryRepository.findById(id);
        category1.get().setActivated(false);
        category1.get().setDeleted(true);
        categoryRepository.save(category1.get());
    }

    @Override
    public void enableById(Long id) {
        Optional<Category> category1 = categoryRepository.findById(id);
        category1.get().setActivated(true);
        category1.get().setDeleted(false);
        categoryRepository.save(category1.get());
    }

    @Override
    public List<CategoryDto> getCategorySize() {
        return categoryRepository.getCategorySize();
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findCategoryById(id);
    }
}
