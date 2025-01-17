package com.gathering.book_review.controller.response;

import com.gathering.book.controller.response.BookResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BookReviewsResponse {

    private List<BookReviewResponse> bookReviewResponses;
    private List<BookResponse> bookResponses;
    private long total;
    private boolean hasNext;

    public static BookReviewsResponse fromEntity(List<BookReviewResponse> reviews, Long total) {
        return BookReviewsResponse.builder()
                .bookReviewResponses(reviews)
                .total(total)
                .build();
    }

    public static BookReviewsResponse fromEntity(List<BookResponse> bookResponses, List<BookReviewResponse> bookReviewResponses, long total) {
        return BookReviewsResponse.builder()
                .bookResponses(bookResponses)
                .bookReviewResponses(bookReviewResponses)
                .total(total)
                .build();
    }

    public static BookReviewsResponse fromBookReviews(List<BookReviewResponse> reviews, boolean hasNext) {
        return BookReviewsResponse.builder()
                .bookReviewResponses(reviews)
                .hasNext(hasNext)
                .build();
    }

}
