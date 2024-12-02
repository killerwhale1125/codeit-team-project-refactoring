package com.gathering.category.model.entity;

import com.gathering.book.model.entity.BookCategory;
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
}
