package com.gathering.book.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.book.model.entity.Book;
import com.gathering.user.model.entitiy.UserAttendanceBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Builder
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String publisherDate;
    private double star;
    private String image;

    // 독서 리뷰 페이지에서 작성하지 않은 리뷰 목록을 위해 모임 아이디 추가
    private Long gatheringId;

    // 독서 리뷰 상세 페이지의 해당 책에 대한 모임 여부 추가
    private boolean gatheringExists;

    // 추천 검색어 생성자
    public BookResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public BookResponse(Long id, String title, String author, String publisher, String publisherDate, double star, String image, Long gatheringId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publisherDate = publisherDate;
        this.star = star;
        this.image = image;
        this.gatheringId = gatheringId;
    }

    // 독서 리뷰 상세 페이지 책 정보 생성자
    public BookResponse(Long id, String title, String author, String publisher, String publisherDate, double star, String image, boolean gatheringExists) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publisherDate = publisherDate;
        this.star = star;
        this.image = image;
        this.gatheringExists = gatheringExists;
    }

    public static BookResponse fromEntity(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .publisherDate(book.getPublishDate())
                .star(book.getStar())
                .image(book.getImage())
                .build();
    }

    public static BookResponse fromEntity(UserAttendanceBook userAttendanceBook) {
        return BookResponse.builder()
                .id(userAttendanceBook.getBook().getId())
                .image(userAttendanceBook.getBook().getImage())
                .build();
    }

    public void ChangeFormat(String publisherDate) {
        this.publisherDate = publisherDate.replace("년 ", ".").replace("월","");
    }
}
