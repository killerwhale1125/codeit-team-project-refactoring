package com.gathering.book.controller.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookSearchResponse {
    private Long bookId;
    private String title;
    private String bookImage;
    private long gatheringCount;

    @QueryProjection
    public BookSearchResponse(Long bookId, String title, String bookImage, long gatheringCount) {
        this.bookId = bookId;
        this.title = title;
        this.bookImage = bookImage;
        this.gatheringCount = gatheringCount;
    }
}
