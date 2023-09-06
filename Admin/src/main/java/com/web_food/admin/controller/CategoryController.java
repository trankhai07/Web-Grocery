package com.web_food.admin.controller;

import com.nimbusds.jose.proc.SecurityContext;
import com.web_food.library.model.Category;
import com.web_food.library.repository.CategoryRepository;
import com.web_food.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String categories(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }

        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories",categoryList);
        model.addAttribute("size",categoryList.size());
        model.addAttribute("title","Category");
        model.addAttribute("categorynew",new Category());
        return "categories";
    }

    @PostMapping("/categories")
    public String save(@ModelAttribute("categorynew") Category category, Model model, RedirectAttributes redirectAttributes){

        try{
            categoryService.save(category);
            redirectAttributes.addFlashAttribute("success","Add Successfully");
        }catch (DataIntegrityViolationException e1){
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error","Add Failed");
        }
        return "redirect:/categories";
    }

    @RequestMapping("/findById")
    @ResponseBody
    public Optional<Category> findById(@RequestParam("id") Long id){
        return categoryService.findById(id);
    }

    @GetMapping("/update-categories")
    public String update(Category category,RedirectAttributes redirectAttributes){

        try{
            categoryService.update(category);
            redirectAttributes.addFlashAttribute("success","Update Successfully");
        }catch (DataIntegrityViolationException e1){
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error","Update Failed");
        }
        return "redirect:/categories";
    }

    @RequestMapping("delete-categories")
 //  @ResponseBody
    public String delete(@RequestParam("id")  Long id,RedirectAttributes redirectAttributes){
        try{
            categoryService.deleteById(id);
            redirectAttributes.addFlashAttribute("success","Delete Successfully");
        }catch (DataIntegrityViolationException e1){
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error","Delete Failed");
        }
        return "redirect:/categories";
    }

    @RequestMapping("enable-categories")
   // @ResponseBody
    public String enable(@RequestParam("id") Long id,RedirectAttributes redirectAttributes){
        try{
            categoryService.enableById(id);
            redirectAttributes.addFlashAttribute("success","Enable Successfully");
        }catch (DataIntegrityViolationException e1){
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error","Enable Failed");
        }
        return "redirect:/categories";
    }
}
