package com.web_food.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web_food.library.dto.ProductDto;
import com.web_food.library.model.CartItem;
import com.web_food.library.model.Customer;
import com.web_food.library.model.Discount;
import com.web_food.library.model.ShoppingCart;
import com.web_food.library.service.CustomerService;
import com.web_food.library.service.DicountService;
import com.web_food.library.service.ProductService;
import com.web_food.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.security.Principal;
import java.util.Set;

@Controller
public class ShoppingCartController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private DicountService dicountService;
    @GetMapping("/cart")
    public String cart(Model model, Principal principal, @RequestParam(value = "discount", required = false, defaultValue = "") String discount){
        if(principal == null){
            return "redirect:/login";
        }else{
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart shoppingCart = customer.getCart();
            if(shoppingCart == null || shoppingCart.getCartItems().size() == 0){
                model.addAttribute("check",1);
            }
            else{
                if(!discount.equals("")){
                    Discount discount1 = dicountService.findByCode(discount);
                    model.addAttribute("Dis",discount1);
                }
                model.addAttribute("check",2);

            }
            model.addAttribute("shoppingCart",shoppingCart);
            }

            model.addAttribute("title","Cart");
            return "cart";
        }


    @PostMapping("/add-to-cart")
    public String addItemtoCart(@RequestParam("id") Long id, @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                                HttpSession session, HttpServletRequest httpServletRequest,Principal principal, Model model){
        ProductDto productDto = productService.findById(id);
        if(principal == null){
            return "redirect:/login";
        }else{
            String username = principal.getName();

            ShoppingCart shoppingCart = shoppingCartService.addItemtoCart(productDto,quantity,username);

            model.addAttribute("shoppingCart",shoppingCart);

            Set<CartItem> cartItemSet = shoppingCart.getCartItems();
            session.setAttribute("grandTotal",cartItemSet.size());
        }
        return "redirect:" + httpServletRequest.getHeader("Referer"); // trả về url của trang dẫn đến
    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(@RequestParam("id") Long id, @RequestParam(value = "quantity", required = false,defaultValue = "1") int quantity,
                             Principal principal, Model model, HttpSession session, @RequestBody String json){
        ProductDto productDto = productService.findById(id);
        if(principal == null){
            return "redirect:/login";
        }
        else{
            if(quantity > productDto.getCurrentQuantity()){ // check quantity in inventory
                session.setAttribute("CheckQuantity", 0);
                session.setAttribute("message",productDto.getName()+" is over quantity (The remaining amount "+ productDto.getCurrentQuantity()+")");
            }
            else{
                String username = principal.getName();
                ShoppingCart shoppingCart = shoppingCartService.updateCart(productDto,quantity,username);
                model.addAttribute("shoppingCart",shoppingCart);
                session.setAttribute("CheckQuantity", 1);
                session.setAttribute("message",productDto.getName()+" updated successfully");
            }
            return "redirect:/cart";
        }
    }

//    @PostMapping("/update-cart")
//    public void updateCart( Principal principal, HttpSession session, @RequestBody String json, HttpServletResponse response) throws IOException {
//        long id = -1;
//        int quantity = 1;
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//            JsonNode jsonNode = objectMapper.readTree(json);
//                    id = jsonNode.get("id").asInt();
//                    quantity = jsonNode.get("quantity").asInt();
//
//            ProductDto productDto = productService.findById(id);
//        if(principal == null){
//            response.sendRedirect("/login");
//        }
//        else{
//
//            if(quantity > productDto.getCurrentQuantity()){ // check quantity in inventory
//                ObjectMapper mapper = new ObjectMapper();
//                session.setAttribute("CheckQuantity", 0);
//                session.setAttribute("message",productDto.getName()+" is over quantity (The remaining amount "+ productDto.getCurrentQuantity()+")");
//                mapper.writeValue(response.getOutputStream(),"Wrong");
//            }
//            else{
//                ObjectMapper mapper = new ObjectMapper();
//                String username = principal.getName();
//                ShoppingCart shoppingCart = new ShoppingCart();
//                shoppingCart = shoppingCartService.updateCart(productDto,quantity,username);
//                session.setAttribute("CheckQuantity", 1);
//                session.setAttribute("message",productDto.getName()+" updated successfully");
//                mapper.writeValue(response.getOutputStream(),"success");
//            }
//
//        }
//
//    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
    public String deleteCart(@RequestParam("id") Long id, @RequestParam(value = "quantity", required = false,defaultValue = "1") int quantity,
                             Principal principal, Model model, HttpSession session){
        ProductDto productDto = productService.findById(id);
        if(principal == null){
            return "redirect:/login";
        }
        else{
            String username = principal.getName();
            ShoppingCart shoppingCart = shoppingCartService.deleteCart(productDto,quantity,username);

            model.addAttribute("shoppingCart",shoppingCart);
            Set<CartItem> cartItemSet = shoppingCart.getCartItems();
            session.setAttribute("grandTotal",cartItemSet.size());
            session.removeAttribute("message");
            return "redirect:/cart";
        }
    }
}
