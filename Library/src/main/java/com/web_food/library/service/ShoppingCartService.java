package com.web_food.library.service;

import com.web_food.library.dto.ProductDto;
import com.web_food.library.model.Discount;
import com.web_food.library.model.ShoppingCart;

import java.util.Optional;

public interface ShoppingCartService {
    ShoppingCart addItemtoCart(ProductDto productDto, int quantity, String username);
    ShoppingCart updateCart(ProductDto productDto,int quantity, String username);
    ShoppingCart deleteCart(ProductDto productDto, int quantity, String username);

    Optional<ShoppingCart> findById(Long id);
    ShoppingCart deleteAllCart(Long id);

}
