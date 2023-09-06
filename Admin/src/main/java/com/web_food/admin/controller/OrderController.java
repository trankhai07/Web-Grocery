package com.web_food.admin.controller;

import com.web_food.library.model.Customer;
import com.web_food.library.model.Order;
import com.web_food.library.service.CustomerService;
import com.web_food.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/order")
    public String getOrders(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
            List<Order> orderList = orderService.findAll();
            model.addAttribute("orders",orderList);
            model.addAttribute("title","OrderAdmin");
            model.addAttribute("page","OrderAdmin");

            return "Order";

    }

    @GetMapping("/accept-order")
    public String acceptOrder(@RequestParam(value = "id",required = false,defaultValue = "-1") Long id, RedirectAttributes redirectAttributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }

        if(id != -1){
            orderService.AcceptById(id);
            redirectAttributes.addFlashAttribute("success","Update successfully!");
        }
        return "redirect:/order";
    }
    @GetMapping("/fail-order")
    public String failOrder(@RequestParam(value = "id",required = false,defaultValue = "-1") Long id, RedirectAttributes redirectAttributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }

        if(id != -1){
            orderService.FailById(id);
            redirectAttributes.addFlashAttribute("success","Update successfully!");
        }
        return "redirect:/order";
    }

    @GetMapping("/cancel-order")
    public String cancelOrder(@RequestParam(value = "id", required = false,defaultValue = "-1") Long id, RedirectAttributes redirectAttributes){
        if(id != -1){
            orderService.CancelById(id);
            redirectAttributes.addFlashAttribute("success","Update successfully!");
        }
        return "redirect:/order";
    }
}
