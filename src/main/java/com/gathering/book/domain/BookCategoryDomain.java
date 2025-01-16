package com.gathering.book.domain;

import com.gathering.category.domain.CategoryDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookCategoryDomain {
    private Long id;
    private BookDomain book;
    private CategoryDomain category;
}
