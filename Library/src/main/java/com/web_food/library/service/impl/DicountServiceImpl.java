package com.web_food.library.service.impl;

import com.web_food.library.model.Discount;
import com.web_food.library.repository.DiscountRepository;
import com.web_food.library.service.DicountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
public class DicountServiceImpl implements DicountService {
    @Autowired
    private DiscountRepository discountRepository;
    @Override
    public Discount findByCode(String code) {
        return discountRepository.findByCode(code);
    }

    @Override
    public void delete(Discount discount) {
        discount.setDis_active(false);
        discountRepository.save(discount);
    }
}
