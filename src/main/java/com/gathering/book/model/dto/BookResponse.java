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
