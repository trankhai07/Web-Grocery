package com.web_food.library.service.impl;

import com.web_food.library.dto.ProductDto;
import com.web_food.library.model.Product;
import com.web_food.library.repository.ProductRepository;
import com.web_food.library.service.ProductService;
import com.web_food.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageUpload imageUpload;
    @Override
    public List<ProductDto> findAll() {
        List<Product> product = productRepository.findAll();
        return fromProducttoProductDto(product);
    }

    @Override
    public ProductDto findById(Long id) {

        Optional<Product> product = productRepository.findById(id);
        List<Product> products = new ArrayList<>();
        products.add(product.get());

        return fromProducttoProductDto(products).get(0);
    }

    @Override
    public void save(MultipartFile file,ProductDto productDto) {
        Product product = new Product();

        try{
            if(file == null){
                product.setImage(null);
            }else{
                imageUpload.upLoadFile(file);
                product.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCost(productDto.getCost());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCategory(productDto.getCategory());
            product.setIs_activated(true);
            product.setIs_deleted(false);

            productRepository.save(product);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void update(MultipartFile file,ProductDto productDto) {
        Optional<Product> product = productRepository.findById(productDto.getId());
        try{
            if(file.getOriginalFilename() == ""){
                product.get().setImage(product.get().getImage());
            }else{
                if(imageUpload.checkexit(file) == false)
                    imageUpload.upLoadFile(file);
                product.get().setImage(Base64.getEncoder().encodeToString(file.getBytes()));
            }
            product.get().setName(productDto.getName());
            product.get().setDescription(productDto.getDescription());
            product.get().setCost(productDto.getCost());
            product.get().setSale(productDto.getSale());
            product.get().setCurrentQuantity(productDto.getCurrentQuantity());
            product.get().setCategory(productDto.getCategory());
            product.get().setIs_activated(true);
            product.get().setIs_deleted(false);

            productRepository.save(product.get());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        product.get().setIs_deleted(true);
        product.get().setIs_activated(false);

        productRepository.save(product.get());
    }

    @Override
    public void enableById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        product.get().setIs_deleted(false);
        product.get().setIs_activated(true);

        productRepository.save(product.get());
    }


    @Override
    public Page<ProductDto> pageProducts(int PageNo) {
        Pageable pageable = PageRequest.of(PageNo,8);
        List<Product> productDtos = productRepository.findAll();
        Page<ProductDto> productPage = toPage(fromProducttoProductDto(productDtos),pageable);
        return productPage;
    }

    @Override
    public Page<ProductDto> searchProducts(String keyword, int PageNo) {
        Pageable pageable = PageRequest.of(PageNo,5);
        List<Product> products = productRepository.searchProductList(keyword);
        List<ProductDto> productDtos = this.fromProducttoProductDto(products);
        Page<ProductDto> productPage = this.toPage(productDtos,pageable);
        return productPage;
    }

    @Override
    public Page<ProductDto> searchProductsByCategory(String keyword, int PageNo) {
        Pageable pageable = PageRequest.of(PageNo,5);
        List<Product> products = productRepository.findAllByCategoryName(keyword);
        List<ProductDto> productDtos = this.fromProducttoProductDto(products);
        Page<ProductDto> productPage = this.toPage(productDtos,pageable);
        return productPage;
    }

    @Override
    public List<ProductDto> findAllByCategoryName(String category) {
        return fromProducttoProductDto(productRepository.findAllByCategoryName(category));
    }

    @Override
    public List<ProductDto> listViewProduct() {
        return fromProducttoProductDto(productRepository.listViewProduct());
    }

    @Override
    public List<ProductDto> randomProduct() {
        return fromProducttoProductDto(productRepository.randomProduct());
    }

    @Override
    public List<ProductDto> findByCategoryId(Long id) {
        return fromProducttoProductDto(productRepository.findByCategoryId(id));
    }

    private Page toPage(List<ProductDto> list,Pageable pageable){
        if(pageable.getOffset() >= list.size()){
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size()) ? list.size():
                (int) (pageable.getOffset() + pageable.getPageSize());
        List sublist = list.subList(startIndex,endIndex);
        return new PageImpl(sublist,pageable,list.size());
    }
    private List<ProductDto> fromProducttoProductDto(List<Product> products){
        List<ProductDto> productDtos = new ArrayList<>();
        for(Product i : products){
            ProductDto productDto = new ProductDto();
            productDto.setId(i.getId());
            productDto.setName(i.getName());
            productDto.setCost(i.getCost());
            productDto.setDescription(i.getDescription());
            productDto.setSale(i.getSale());
            productDto.setImage(i.getImage());
            productDto.setCurrentQuantity(i.getCurrentQuantity());
            productDto.setCategory(i.getCategory());
            productDto.setIs_deleted(i.getIs_deleted());
            productDto.setIs_activated(i.getIs_activated());

            productDtos.add(productDto);
        }
        return productDtos;
    }

}
