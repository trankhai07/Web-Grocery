package com.web_food.library.service;

import com.web_food.library.model.Order;
import com.web_food.library.model.ShoppingCart;

import java.util.List;

public interface OrderService {
    Order save(ShoppingCart shoppingCart, String method,String discount);
    void CancelById(Long id);
    List<Order> findAll();

    Order AcceptById(Long id);

    Order FailById(Long id);
}
