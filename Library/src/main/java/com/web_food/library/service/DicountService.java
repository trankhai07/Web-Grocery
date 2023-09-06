package com.web_food.library.service;

import com.web_food.library.model.Discount;

public interface DicountService {
    Discount findByCode(String code);
    void delete (Discount discount);
}
