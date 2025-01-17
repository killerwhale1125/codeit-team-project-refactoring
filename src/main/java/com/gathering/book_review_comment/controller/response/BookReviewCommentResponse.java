package com.gathering.book_review_comment.controller.response;

import com.gathering.book_review_comment.domain.BookReviewCommentDomain;
import com.gathering.book_review.domain.StatusType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookReviewCommentResponse {

    private long id;
    private long userId;
    private long reviewId;
    private String content;
    private long parent;
    private int orders;
    private StatusType status;

    public static BookReviewCommentResponse fromEntity(BookReviewCommentDomain bookReviewComment) {
        return BookReviewCommentResponse.builder()
                .id(bookReviewComment.getId())
                .userId(bookReviewComment.getUser().getId())
                .reviewId(bookReviewComment.getReview().getId())
                .content(bookReviewComment.getContent())
                .parent(bookReviewComment.getParent())
                .status(bookReviewComment.getStatus())
                .build();
    }
}
