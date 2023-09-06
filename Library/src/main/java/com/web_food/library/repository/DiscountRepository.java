package com.web_food.library.repository;

import com.web_food.library.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Long> {

    @Query(value = "Select c from Discount c where c.code = ?1")
    Discount findByCode(String code);
}
