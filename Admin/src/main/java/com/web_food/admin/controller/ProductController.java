package com.web_food.admin.controller;

import com.web_food.library.dto.ProductDto;
import com.web_food.library.model.Category;
import com.web_food.library.model.Product;
import com.web_food.library.service.CategoryService;
import com.web_food.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    CategoryService categoryService;

//    @GetMapping("/products")
//    public String products(Model model){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
//            return "redirect:/login";
//        }
//        List<ProductDto> productDtoList = productService.findAll();
//        model.addAttribute("products",productDtoList);
//        model.addAttribute("title","Product");
//        model.addAttribute("size",productDtoList.size());
//        return "products";
//    }
    @GetMapping("/products")
    public String products(Model model,@RequestParam("PageNo") int pageno){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.pageProducts(pageno);
        model.addAttribute("products",products);
        model.addAttribute("title","Product");
        model.addAttribute("size",products.getSize());
        model.addAttribute("totalPages",products.getTotalPages());
        model.addAttribute("currentPage",pageno);

        return "products";
    }

    @GetMapping("/search-products")
    public String searchproducts(Model model,@RequestParam("PageNo") int pageno,
                                 @RequestParam("keyword") String keyword){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.searchProducts(keyword,pageno);
        model.addAttribute("keyword",keyword);
        model.addAttribute("products",products);
        model.addAttribute("title","Product");
        model.addAttribute("size",products.getSize());
        model.addAttribute("totalPages",products.getTotalPages());
        model.addAttribute("currentPage",pageno);
        return "search-products";
    }

    @GetMapping("/add-products")
    public String addProducts(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAllActivatedTrue();
        model.addAttribute("product",new ProductDto());
        model.addAttribute("categories",categories);
        return "add-products";
    }
    @PostMapping("/products")
    public String saveProduct(@ModelAttribute("product") ProductDto productDto,
                              RedirectAttributes redirectAttributes,
                              @RequestParam("imageProduct") MultipartFile multipartFile){
        try{
            productService.save(multipartFile,productDto);
            redirectAttributes.addFlashAttribute("success", "Add product successfully");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error","Failed");
        }
        return "redirect:/products?PageNo=0";
    }

    @GetMapping("/update-product")
    public String updateProductForm(@RequestParam("id") Long id,Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        ProductDto productDtos = productService.findById(id);
        List<Category> categories = categoryService.findAllActivatedTrue();
        model.addAttribute("title","Update product");
        model.addAttribute("product",productDtos);
        model.addAttribute("categories",categories);

        return "update-products";
    }

    @PostMapping("/update-product")
    public String updateProdcut( @RequestParam("imageProduct") MultipartFile file,
                                @ModelAttribute("product") ProductDto productDto,
                                RedirectAttributes redirectAttributes){
        try{
            redirectAttributes.addFlashAttribute("success","Update successfully");
            productService.update(file, productDto);
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error","Faild");
        }

        return "redirect:/products?PageNo=0";
    }


    @RequestMapping("/delete-product")
    public String deleteProduct(@RequestParam("id") Long id,
                                RedirectAttributes redirectAttributes){
        try{
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("success","Delete successfully");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error","Failed");
        }
        return "redirect:/products?PageNo=0";
    }
    @RequestMapping("/enable-product")
    public String enableProduct(@RequestParam("id") Long id,
                                RedirectAttributes redirectAttributes){
        try{
            productService.enableById(id);
            redirectAttributes.addFlashAttribute("success","Enable successfully");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error","Failed");
        }
        return "redirect:/products?PageNo=0";

    }
}
