package com.web_food.customer.controller;

import com.web_food.library.dto.CustomerDto;
import com.web_food.library.model.*;
import com.web_food.library.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller

public class OrderController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CityService cityService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private DicountService dicountService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/check-out")
    public String checkOut(Model model, Principal principal,@RequestParam(value = "discount", required = false, defaultValue = "") String discount){
        if(principal == null){
            return "redirect:/login";
        }
        else{
            CustomerDto customerDto = customerService.getCustomer(principal.getName());
            if(customerDto.getAddress() == null || customerDto.getCountry() == null || customerDto.getCity() == null || customerDto.getPhoneNumber() == null){
                model.addAttribute("information", "You need update your information before check out");

                List<City> cityList = cityService.findAll();
                List<Country> countryList = countryService.findAll();

                model.addAttribute("customer",customerDto);
                model.addAttribute("cities",cityList);
                model.addAttribute("countries",countryList);
                model.addAttribute("title", "Profile");
                model.addAttribute("page", "Profile");
                return "Myaccount";
            }
            else{
                Customer customer = customerService.findByUsername(principal.getName());
                ShoppingCart shoppingCart = customer.getCart();
                if(!discount.equals("")){
                    Discount discount1 = dicountService.findByCode(discount);
                    model.addAttribute("Dis",discount1);
                }
                model.addAttribute("shoppingCart",shoppingCart);
                model.addAttribute("country",countryService.findById(customerDto.getCountry()));
                model.addAttribute("customer",customerDto);
                model.addAttribute("title", "Check-Out");
                model.addAttribute("page", "Check-Out");

                return "checkout";
            }
        }

    }

    @GetMapping("/order")
    public String getOrders(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        else{
            Customer customer = customerService.findByUsername(principal.getName());
            List<Order> orderList = customer.getOrders();
            model.addAttribute("orders",orderList);
            model.addAttribute("title","Order");
            model.addAttribute("page","Order");
            return "Order";
        }
    }

    @GetMapping("/cancel-order")
    public String cancelOrder(@RequestParam(value = "id", required = false,defaultValue = "-1") Long id, RedirectAttributes redirectAttributes){
        if(id != -1){
            orderService.CancelById(id);
            redirectAttributes.addFlashAttribute("success","Delete successfully!");
        }
        return "redirect:/order";

    }
    @GetMapping("/fail-order")
    public String failOrder(@RequestParam(value = "id",required = false,defaultValue = "-1") Long id, RedirectAttributes redirectAttributes){
        if(id != -1){
            orderService.FailById(id);

        }
        return "redirect:/order";
    }

    @PostMapping("/add-order")
    public String addOrder(Model model, Principal principal, @RequestParam("method") String method,
                           @RequestParam(value = "discount", required = false, defaultValue = "") String discount,
                           HttpSession session, HttpServletResponse response, HttpServletRequest request){
        if(principal == null){
            return "redirect:/login";
        }
        else{
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart shoppingCart = customer.getCart();
            Order order = orderService.save(shoppingCart,method,discount);

            if(method.equals("VNPAY")){
                final int[] seconds = {60};  // do run là overide của TimerTask nên int là biến local không thể truy cập tới, phải là final

                Timer timer = new Timer();

                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        seconds[0]--;
                        System.out.println(seconds[0]);
                        if(seconds[0] < 0){
                            timer.cancel();
                            orderService.FailById(order.getId());
                        }
                    }
                },0,1000);

            }
            session.removeAttribute("grandTotal");
            return "redirect:/order";
        }
    }




}
