package com.web_food.customer.controller;

import com.web_food.library.dto.CustomerDto;
import com.web_food.library.model.City;
import com.web_food.library.model.Country;
import com.web_food.library.model.Customer;
import com.web_food.library.service.CityService;
import com.web_food.library.service.CountryService;
import com.web_food.library.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import java.security.Principal;
import java.util.List;

@Controller
public class AccountController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CityService cityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CountryService countryService;
    @GetMapping("/profile")
    public String Profile(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        else{
            String username = principal.getName();
            CustomerDto customerDto = customerService.getCustomer(username);
            List<City> cityList = cityService.findAll();
            List<Country> countryList = countryService.findAll();

            model.addAttribute("customer",customerDto);
            model.addAttribute("cities",cityList);
            model.addAttribute("countries",countryList);
            model.addAttribute("title", "Profile");
            model.addAttribute("page", "Profile");
            return "Myaccount";
        }
    }
    @PostMapping("/update-profile")
    public  String updateProfile(@Valid @ModelAttribute("customer") CustomerDto customerDto,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                 Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }else{
            List<City> cityList = cityService.findAll();
            List<Country> countryList = countryService.findAll();
            model.addAttribute("cities",cityList);
            model.addAttribute("countries",countryList);
            if(bindingResult.hasErrors()){
                return "Myaccount";
            }
            customerService.update(customerDto);
            redirectAttributes.addFlashAttribute("success","Update Successfully!");
            return "redirect:/profile";
        }
     }

     @GetMapping("/change-password")
     public  String changePassword(Model model, Principal principal){
         if (principal == null) {
             return "redirect:/login";
         }
         model.addAttribute("title", "Change password");
         model.addAttribute("page", "Change password");
         return "changepassword";
     }

     @PostMapping("/change-password")
     public String changePass(Model model, Principal principal, @RequestParam("oldPassword")String oldpassword,
                                                         @RequestParam("newPassword")String newpassword,
                                                         @RequestParam("verifyPassword")String verifyPassword,
                                                         RedirectAttributes redirectAttributes){
        if(principal == null){
            return "redirect:/login";
        }
        else{
            String username = principal.getName();
            CustomerDto customerDto = customerService.getCustomer(username);
            if(passwordEncoder.matches(oldpassword,customerDto.getPassword()) &&
               !passwordEncoder.matches(newpassword,customerDto.getPassword()) &&
                newpassword.equals(verifyPassword) && newpassword.length() >= 5){
                customerDto.setPassword(passwordEncoder.encode(newpassword));

                customerService.changPass(customerDto);
                redirectAttributes.addFlashAttribute("success", "Your password has been changed successfully!");
                return "redirect:/profile";
            }
            else{
                model.addAttribute("message", "Your password is wrong");
                return "changepassword";
            }

        }
     }

}
