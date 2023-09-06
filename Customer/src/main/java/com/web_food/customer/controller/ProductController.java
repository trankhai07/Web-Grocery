package com.web_food.customer.controller;

import com.web_food.library.dto.CategoryDto;
import com.web_food.library.dto.ProductDto;
import com.web_food.library.model.Category;
import com.web_food.library.service.CategoryService;
import com.web_food.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/menu")
    public String menu(Model model){
        model.addAttribute("title","Menu");
        model.addAttribute("page","Product");
        List<ProductDto> productDtoList = productService.findAll();
        List<Category> categories = categoryService.findAllActivatedTrue();

        model.addAttribute("categories",categories);
        model.addAttribute("products",productDtoList);
         return "index";
    }


    @GetMapping("/shop-detail")
    public String ShopDetail(Model model,@RequestParam("PageNo") int pageno){
        List<CategoryDto> categoryDtos = categoryService.getCategorySize();
        model.addAttribute("categories",categoryDtos);

        Page<ProductDto> products = productService.pageProducts(pageno);;
        model.addAttribute("check",1);
        model.addAttribute("listView",products);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");

        model.addAttribute("size",products.getSize());
        model.addAttribute("totalPages",products.getTotalPages());
        model.addAttribute("currentPage",pageno);

        return "shop";
    }
    @GetMapping("/find-products")
    public String productsInCategory(Model model,@RequestParam("id") Long id,@RequestParam("PageNo") int pageno
                                     ){
        List<CategoryDto> categoryDtos = categoryService.getCategorySize();
        model.addAttribute("categories",categoryDtos);
        Category category = categoryService.findCategoryById(id);
        Page<ProductDto> products = productService.searchProductsByCategory(category.getName(),pageno);

        model.addAttribute("products",products);
        model.addAttribute("category_name",category);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");

        model.addAttribute("size",products.getSize());
        model.addAttribute("totalPages",products.getTotalPages());
        model.addAttribute("currentPage",pageno);
        return "product";
    }
    @GetMapping("/product-detail")
    public String productDetail(Model model,@RequestParam("id") Long id){

        ProductDto productDto = productService.findById(id);
        List<ProductDto> productRelated = productService.findByCategoryId(productDto.getCategory().getId());
        model.addAttribute("product",productDto);
        model.addAttribute("title","Product detail");
        model.addAttribute("productRelated",productRelated);
        return "product-single";
    }
}
