package com.gathering.category.repository;

import com.gathering.category.model.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository{

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
