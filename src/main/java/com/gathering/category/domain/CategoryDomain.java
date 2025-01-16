package com.gathering.category.domain;

import com.gathering.book.infrastructure.entity.BookCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CategoryDomain {
    private Long id;
    private List<BookCategory> bookCategories;
    private String name;
}
