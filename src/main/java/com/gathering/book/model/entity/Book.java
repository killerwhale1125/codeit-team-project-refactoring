package com.gathering.book.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    private String title;
    private String image;
    private String publisher;
    private String author;

    private String publishDate;
    private long selectedCount;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    List<BookCategory> bookCategories = new ArrayList<>();

    public static Book createBook(String title, String image, String author, String publisher, BookCategory bookCategory, String publisherDate) {
        Book book = new Book();
        book.title = title;
        book.image = image;
        book.author = author;
        book.publisher = publisher;
        book.publishDate = publisherDate;
        book.bookCategories.add(bookCategory);
        bookCategory.addBook(book);
        return book;
    }

    // 모임에 책이 선택될 때마다 카운트를 증가 ( Best 독서 모임 조회용 )
    public void incrementSelectedCount() {
        this.selectedCount++;
    }
}
