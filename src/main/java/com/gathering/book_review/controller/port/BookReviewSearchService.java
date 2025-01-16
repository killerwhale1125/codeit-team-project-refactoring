package com.gathering.book_review.controller.port;

import com.gathering.book_review.controller.response.BookReviewsResponse;

public interface BookReviewSearchService {
    BookReviewsResponse myReviews(String username, int page, int size);
}
