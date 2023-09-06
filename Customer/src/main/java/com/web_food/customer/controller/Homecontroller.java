package com.web_food.customer.controller;

import com.web_food.library.dto.ProductDto;
import com.web_food.library.model.CartItem;
import com.web_food.library.model.Customer;
import com.web_food.library.model.ShoppingCart;
import com.web_food.library.service.CustomerService;
import com.web_food.library.service.ProductService;
import com.web_food.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class Homecontroller {
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @RequestMapping(value = {"/index","/"}, method = RequestMethod.GET)
    public String home (Model model, Principal principal, HttpSession httpSession){
        List<ProductDto> productDtoList = productService.randomProduct();
        model.addAttribute("products",productDtoList);
        model.addAttribute("title", "Home");
        model.addAttribute("page", "Home");

        if(principal != null){
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart shoppingCart = customer.getCart();
            if(shoppingCart != null){
                Set<CartItem> cartItemSet = shoppingCart.getCartItems();
                httpSession.setAttribute("grandTotal",cartItemSet.size());
            }
            httpSession.setAttribute("username",customer.getFirstName()+" "+customer.getLastName());
        }
        return "index";
    }
}
