package com.gathering.book.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.book.model.entity.BookCategory;
import com.gathering.review.model.entitiy.BookReview;
import com.gathering.user.model.entitiy.UserAttendanceBook;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDomain {
    private Long id;

    private String title;
    private String image;
    private String publisher;
    private String author;
    private String publishDate;
    private long selectedCount;
    private double star;

    private String introduce;

    private int totalPage;

    List<BookCategory> bookCategories = new ArrayList<>();

    List<BookReview> reviews = new ArrayList<>();

    List<UserAttendanceBook> userAttendanceBooks = new ArrayList<>();

    public void incrementSelectedCount() {
        
    }
}
