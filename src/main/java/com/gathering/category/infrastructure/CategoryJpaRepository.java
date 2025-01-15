package com.gathering.category.infrastructure;

import com.gathering.category.infrastructure.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String categoryName);
}
