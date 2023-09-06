package com.web_food.customer.controller;

import com.web_food.library.model.Discount;
import com.web_food.library.model.ShoppingCart;
import com.web_food.library.service.DicountService;
import com.web_food.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class DiscountController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private DicountService dicountService;

    @PostMapping("discount-cart")
    public String discountCart(RedirectAttributes redirectAttribute, Model model, @RequestParam("discount") String discount, @RequestParam("id") Long id, HttpSession session){
        Discount discount1 = dicountService.findByCode(discount);
        if(discount1 == null){
            session.setAttribute("CheckQuantity", 2);
            session.setAttribute("message","Discount not exist");
            return "redirect:/cart";
        }
        else if(discount1.isDis_active() == false){
            session.setAttribute("CheckQuantity", 2);
            session.setAttribute("message","Discount used");
            return "redirect:/cart";
        }
        else{
            session.setAttribute("CheckQuantity", 3);
            session.setAttribute("message","Successfully applied discount code");

            redirectAttribute.addAttribute("discount",discount);
            return "redirect:/cart";
        }

    }
}
