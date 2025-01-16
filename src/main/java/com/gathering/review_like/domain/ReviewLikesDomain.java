package com.gathering.review_like.domain;

import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.user.infrastructure.entitiy.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewLikesDomain {
    private long id;
    private User user;
    private BookReviewDomain review;
}
