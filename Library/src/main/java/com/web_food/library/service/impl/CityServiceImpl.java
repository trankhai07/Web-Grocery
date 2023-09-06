package com.web_food.library.service.impl;

import com.web_food.library.model.City;
import com.web_food.library.repository.CityRepository;
import com.web_food.library.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    @Autowired
    private CityRepository cityRepository;
    @Override
    public List<City> findAll() {
       List<City> cityList = cityRepository.findAll();
        return cityList;
    }
}
