package com.gathering.book.model.entity;

import com.gathering.book.model.domain.BookDomain;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.review.model.entitiy.BookReview;
import com.gathering.user.model.entitiy.UserAttendanceBook;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
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

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    List<BookCategory> bookCategories = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    List<BookReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    List<UserAttendanceBook> userAttendanceBooks = new ArrayList<>();

    public static Book createBook(String title, String image, String author, String publisher, BookCategory bookCategory, String publisherDate, double star, String introduce, int page) {
//        Book book = new Book();
//        book.title = title;
//        book.image = image;
//        book.author = author;
//        book.publisher = publisher;
//        book.publishDate = publisherDate;
//        book.star = star;
//        book.introduce = introduce;
//        book.totalPage = page;
//        book.bookCategories.add(bookCategory);
//        bookCategory.addBook(book);
        return null;
    }

    // 모임에 책이 선택될 때마다 카운트를 증가 ( Best 독서 모임 조회용 )
    public void incrementSelectedCount() {
        this.selectedCount++;
    }

    public BookDomain toEntity() {
        return BookDomain.builder()
                .id(id)
                .title(title)
                .image(image)
                .publisher(publisher)
                .publishDate(publishDate)
                .selectedCount(selectedCount)
                .star(star)
                .introduce(introduce)
                .totalPage(totalPage)
                .build();
    }
}
