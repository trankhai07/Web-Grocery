package com.web_food.admin.controller;

import com.web_food.library.dto.AdminDto;
import com.web_food.library.model.Admin;
import com.web_food.library.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AdminService adminService;


    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("title","Login Admin");
        return "login";
    }

    @RequestMapping("/index")
    public String home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        model.addAttribute("title","Home Admin");
        return "index";
    }

    @GetMapping("/register")
    public String registerForm(Model model,HttpSession session){
        session.removeAttribute("message");
        model.addAttribute("adminDto",new AdminDto());
        model.addAttribute("title","Register Admin");
        return "register";
    }
    @GetMapping("/forgot-password")
    public String forgotForm(Model model){
        model.addAttribute("title","Forgot password");
        return "forgot-password";
    }

    @PostMapping("/register-new")
    public String addNewAdmin (@Valid @ModelAttribute("adminDto") AdminDto adminDto,
                               BindingResult result, Model model, HttpSession session){

        try{
            session.removeAttribute("message");
            if(result.hasErrors()){
                model.addAttribute("adminDto",adminDto);
                result.toString();
                return "register";
            }
            String username = adminDto.getUsername();
            Admin admin = adminService.findByUsername(username);

            if(admin != null){
                model.addAttribute("adminDto",adminDto);
                session.setAttribute("message","User name has been registered");
                session.setAttribute("alert","danger");
                return "register";
            }

            if(adminDto.getPassword().equals(adminDto.getRepeatPassword())){
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                adminService.save(adminDto);

                session.setAttribute("message","Register Successfully");
                session.setAttribute("alert","success");
                model.addAttribute("adminDto",adminDto);
            }
            else{
                session.setAttribute("message","Your password not same");
                session.setAttribute("alert","danger");
                model.addAttribute("adminDto",adminDto);
                return "register";
            }
        }catch(Exception e){
            e.printStackTrace();
            session.setAttribute("message","The server has been wrong!");
            session.setAttribute("alert","danger");
        }
        return "register";
    }
}
