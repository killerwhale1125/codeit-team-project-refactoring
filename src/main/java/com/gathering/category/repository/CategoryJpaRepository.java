package com.gathering.category.repository;

import com.gathering.category.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String categoryName);
}
