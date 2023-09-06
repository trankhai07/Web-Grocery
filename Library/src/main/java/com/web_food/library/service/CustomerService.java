package com.web_food.library.service;

import com.web_food.library.dto.CustomerDto;
import com.web_food.library.model.Customer;

public interface CustomerService {
    Customer findByUsername(String username);
    Customer save(CustomerDto customerDto);
    Customer update(CustomerDto customerDto);
    Customer changPass(CustomerDto customerDto);

    CustomerDto getCustomer(String username);
}
