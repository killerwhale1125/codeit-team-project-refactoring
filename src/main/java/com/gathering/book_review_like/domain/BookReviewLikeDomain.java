package com.gathering.book_review_like.domain;

import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookReviewLikeDomain {
    private Long id;
    private UserDomain user;
    private BookReviewDomain bookReview;

    public static BookReviewLikeDomain create(BookReviewDomain bookReview, UserDomain user) {
        return BookReviewLikeDomain.builder()
                .user(user)
                .bookReview(bookReview)
                .build();
    }
}
