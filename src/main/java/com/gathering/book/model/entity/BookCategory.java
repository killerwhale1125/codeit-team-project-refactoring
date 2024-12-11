package com.gathering.book.model.entity;

import com.gathering.category.model.entity.Category;
import com.gathering.common.base.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public static BookCategory createBookCategory(Category category) {
        BookCategory bookCategory = new BookCategory();
        bookCategory.category = category;
        category.getBookCategories().add(bookCategory);
        return bookCategory;
    }

    public void addBook(Book book) {
        this.book = book;
    }
}
