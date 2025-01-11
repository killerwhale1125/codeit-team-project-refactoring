package com.gathering.book.model.entity;

import com.gathering.book.model.domain.BookDomain;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.review.model.entitiy.BookReview;
import com.gathering.user.model.entitiy.UserAttendanceBook;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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
    private double star;

    @Lob
    @Column(columnDefinition="TEXT")
    private String introduce;

    private int totalPage;

    @OneToMany(mappedBy = "book")
    List<BookCategory> bookCategories = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    List<BookReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    List<UserAttendanceBook> userAttendanceBooks = new ArrayList<>();

    public static Book createBook(String title, String image, String author, String publisher, BookCategory bookCategory, String publisherDate, double star, String introduce, int page) {
        Book book = new Book();
        book.title = title;
        book.image = image;
        book.author = author;
        book.publisher = publisher;
        book.publishDate = publisherDate;
        book.star = star;
        book.introduce = introduce;
        book.totalPage = page;
        book.bookCategories.add(bookCategory);
        bookCategory.addBook(book);
        return book;
    }

    public static Book fromEntity(BookDomain book) {
        Book bookEntity = new Book();
        bookEntity.id = book.getId();
        bookEntity.title = book.getTitle();
        bookEntity.image = book.getImage();
        bookEntity.publisher = book.getPublisher();
        bookEntity.author = book.getAuthor();
        bookEntity.publishDate = book.getPublishDate();
        bookEntity.selectedCount = book.getSelectedCount();
        bookEntity.star = book.getStar();
        bookEntity.introduce = book.getIntroduce();
        bookEntity.totalPage = book.getTotalPage();
        return bookEntity;
    }

    public BookDomain toEntity() {
        return BookDomain.builder()
                .id(id)
                .title(title)
                .image(image)
                .publisher(publisher)
                .author(author)
                .publishDate(publishDate)
                .selectedCount(selectedCount)
                .star(star)
                .introduce(introduce)
                .totalPage(totalPage)
                .build();
    }
}
