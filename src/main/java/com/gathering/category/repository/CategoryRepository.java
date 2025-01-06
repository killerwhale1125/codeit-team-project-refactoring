package com.gathering.category.repository;

import com.gathering.category.model.entity.Category;

public interface CategoryRepository {
    boolean existCategoryByName(String categoryName);

    void save(Category category);
}
