package com.gathering.review_like.domain;

import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewLikesDomain {
    private long id;
    private UserDomain user;
    private BookReviewDomain review;
}
