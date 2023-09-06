package com.web_food.library.repository;

import com.web_food.library.dto.CategoryDto;
import com.web_food.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query(value = "Select * FROM categories  Where is_activated = true",nativeQuery = true)
    List<Category> findAllActivatedTrue();

    @Query(value = "select new com.web_food.library.dto.CategoryDto(c.id, c.name, count(p.category.id)) " +
            "from Category c left join Product p on c.id = p.category.id " +
            "where c.activated = true and c.deleted = false " +
            "group by c.id ")
    List<CategoryDto> getCategorySize();


    @Query(value = "Select c from Category c where c.activated = true and c.deleted = false and c.id = ?1")
    Category findCategoryById(Long id);
}
