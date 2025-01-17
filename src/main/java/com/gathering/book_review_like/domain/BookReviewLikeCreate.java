package com.gathering.book_review_like.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookReviewLikeCreate {
    private Long reviewId;
}
