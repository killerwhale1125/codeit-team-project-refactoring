package com.gathering.book_review.service.port;

import com.gathering.book_review.domain.BookReviewDomain;

public interface BookReviewRepository {

    BookReviewDomain save(BookReviewDomain bookReview);

    BookReviewDomain findById(long reviewId);
}
