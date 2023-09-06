package com.web_food.library.service;

import com.web_food.library.model.Country;

import java.util.List;

public interface CountryService {
    List<Country> findAll();
    Country findById(String id);
}
