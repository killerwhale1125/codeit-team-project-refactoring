package com.gathering.category.infrastructure;

import com.gathering.category.infrastructure.entity.Category;
import com.gathering.category.service.port.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public boolean existCategoryByName(String categoryName) {
        return categoryJpaRepository.existsByName(categoryName);
    }

    @Override
    public void save(Category category) {
        categoryJpaRepository.save(category);
    }
}
