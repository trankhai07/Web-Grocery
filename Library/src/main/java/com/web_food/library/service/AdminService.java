package com.web_food.library.service;

import com.web_food.library.dto.AdminDto;
import com.web_food.library.model.Admin;

public interface AdminService {
    Admin findByUsername(String username);
    Admin save(AdminDto adminDto);
}
