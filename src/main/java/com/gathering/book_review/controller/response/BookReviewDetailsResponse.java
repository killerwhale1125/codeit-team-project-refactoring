package com.gathering.book_review.controller.response;

import com.gathering.book.controller.response.BookResponse;
import com.gathering.book_review_comment.controller.response.BookReviewCommentResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BookReviewDetailsResponse {

    private BookReviewResponse bookReviewResponse;
    private BookResponse bookResponse;
    private List<BookReviewCommentResponse> bookReviewCommentResponses;

    public static BookReviewDetailsResponse fromEntity(BookReviewResponse bookReviewResponse, BookResponse bookResponse, List<BookReviewCommentResponse> bookReviewCommentResponses) {
        return BookReviewDetailsResponse.builder()
                .bookReviewResponse(bookReviewResponse)
                .bookResponse(bookResponse)
                .bookReviewCommentResponses(bookReviewCommentResponses)
                .build();
    }
}
