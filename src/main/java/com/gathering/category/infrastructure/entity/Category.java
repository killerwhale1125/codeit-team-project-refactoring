package com.gathering.category.infrastructure.entity;

import com.gathering.book.infrastructure.entity.BookCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_Id")
    private Long id;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<BookCategory> bookCategories = new ArrayList<>();

    private String name;

    public static Category createCategory(String categoryName) {
        Category category = new Category();
        category.name = categoryName;
        return category;
    }
}
