package com.gathering.book.model.dto;

import com.gathering.book.model.entity.Book;
import lombok.Builder;
import lombok.Getter;

@Getter
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
}
