package com.gathering.book_review.controller.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BookReviewsResponse {

    private List<BookReviewResponse> bookReviewResponses;
    private long total;

    public static BookReviewsResponse fromEntity(List<BookReviewResponse> reviews, Long total) {
        return BookReviewsResponse.builder()
                .bookReviewResponses(reviews)
                .total(total)
                .build();
    }

}
