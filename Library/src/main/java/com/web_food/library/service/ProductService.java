package com.web_food.library.service;

import com.web_food.library.dto.ProductDto;
import com.web_food.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDto> findAll();
    ProductDto findById(Long id);
    void save(MultipartFile file,ProductDto productDto);
    void update(MultipartFile file,ProductDto productDto);
    void deleteById(Long id);
    void enableById(Long id);


    Page<ProductDto> pageProducts(int PageNo);
    Page<ProductDto> searchProducts(String keyword, int PageNo);
    Page<ProductDto> searchProductsByCategory(String keyword, int PageNo);
    List<ProductDto> findAllByCategoryName(String category);
    List<ProductDto> listViewProduct();
    List<ProductDto> randomProduct();
    List<ProductDto> findByCategoryId(Long id);

}
