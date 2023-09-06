package com.web_food.customer.controller;

import com.web_food.library.dto.CustomerDto;
import com.web_food.library.model.Customer;
import com.web_food.library.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private CustomerService customerService;
    @RequestMapping(value = "/login")
    public String login(Model model){
        model.addAttribute("title", "Login Page");
        model.addAttribute("page", "Home");
        return "login";
    }
    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("title", "Register");
        model.addAttribute("page", "Home");
        model.addAttribute("customerDto",new CustomerDto());
        return "register";
    }

    @PostMapping("/do-register")
    public String registerCustomer(@Valid @ModelAttribute("customerDto")CustomerDto customerDto, BindingResult result,Model model){
        try{

            if(result.hasErrors()){
                model.addAttribute("customerDto",customerDto);
                return "register";
            }
            String username = customerDto.getUsername();
            Customer customer = customerService.findByUsername(username);
            if(customer != null){
                model.addAttribute("error","Email has been register!");
                model.addAttribute("customer",customerDto);
                return "register";
            }
            if(customerDto.getPassword().equals(customerDto.getConfirmPassword())){
                customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                customerService.save(customerDto);
                model.addAttribute("success","Register successfully");
            }
            else{
                model.addAttribute("error", "Password is incorrect");
                model.addAttribute("customerDto", customerDto);
                return "register";
            }

        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("error", "Server is error, try again later!");
        }

        return "register";
    }



}
