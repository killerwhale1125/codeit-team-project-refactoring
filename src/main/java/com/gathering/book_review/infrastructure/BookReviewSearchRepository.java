package com.gathering.book_review.infrastructure;

import com.gathering.book_review.controller.response.BookReviewsResponse;
import com.gathering.user.domain.UserDomain;

public interface BookReviewSearchRepository {
    BookReviewsResponse getMyReviews(UserDomain user, int page, int size);
}
