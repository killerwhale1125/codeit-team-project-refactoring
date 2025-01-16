package com.gathering.book.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.book.infrastructure.entity.BookCategory;
import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.user_attendance_book.domain.UserAttendanceBookDomain;
import lombok.Builder;
import lombok.Getter;

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
    List<BookCategory> bookCategories;
    List<BookReviewDomain> reviews;
    List<UserAttendanceBookDomain> userAttendanceBooks;

    // 모임에 책이 선택될 때마다 카운트를 증가 ( Best 독서 모임 조회용 )
    public void incrementSelectedCount() {
        this.selectedCount++;
    }
}
