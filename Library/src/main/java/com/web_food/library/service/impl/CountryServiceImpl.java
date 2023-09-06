package com.web_food.library.service.impl;

import com.web_food.library.model.Country;
import com.web_food.library.repository.CountryRepository;
import com.web_food.library.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {
    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    @Override
    public Country findById(String id) {
        Optional<Country> country = countryRepository.findById(Long.parseLong(id));
        return country.get();
    }

}
