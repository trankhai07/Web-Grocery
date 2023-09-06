package com.web_food.library.service.impl;

import com.web_food.library.model.CartItem;
import com.web_food.library.repository.CartItemRepository;
import com.web_food.library.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Override
    public void deleteById(Long id) {
         cartItemRepository.deleteById(id);
    }
}
