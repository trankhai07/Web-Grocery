package com.web_food.library.repository;

import com.web_food.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "Select p from Product p")
    Page<Product> pageProduct(Pageable pageable);

    @Query(value = "Select p from Product p where p.name like %?1% or p.description like %?1%")
    Page<Product> searchProduct(String keyword, Pageable pageable);

    @Query(value = "Select p from Product p where p.name like %?1% or p.description like %?1%")
    List<Product> searchProductList(String keyword);

    @Query("select p from Product p inner join Category c ON c.id = p.category.id" +
            " where p.category.name = ?1 and p.is_activated = true and p.is_deleted = false")
    List<Product> findAllByCategoryName(String category) ;

    @Query(value = "select p.product_id, p.name, p.description, p.current_quantity, p.cost, p.category_id, p.sale, p.image, p.is_activated, p.is_deleted from products p where p.is_deleted = false and p.is_activated = true limit 12", nativeQuery = true)
    List<Product> listViewProduct();

    @Query(value = "select " +
            "p.product_id, p.name, p.description, p.current_quantity, p.cost, p.category_id, p.sale, p.image, p.is_activated, p.is_deleted " +
            "from products p where p.is_activated = true and p.is_deleted = false order by rand() limit 8", nativeQuery = true)
    List<Product> randomProduct();

    @Query(value = "select p from Product p inner join Category c on c.id = ?1 and p.category.id = ?1 where p.is_activated = true and p.is_deleted = false order by rand() limit 4")
    List<Product> findByCategoryId(Long id);


}
