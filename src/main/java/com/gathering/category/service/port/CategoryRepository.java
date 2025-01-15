package com.gathering.category.service.port;

import com.gathering.category.infrastructure.entity.Category;

public interface CategoryRepository {
    boolean existCategoryByName(String categoryName);

    void save(Category category);
}
