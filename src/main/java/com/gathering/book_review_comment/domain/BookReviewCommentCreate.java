package com.gathering.book_review_comment.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookReviewCommentCreate {
    @NotNull
    private long reviewId;

    @NotEmpty
    private String content;

    private long parent;
}
