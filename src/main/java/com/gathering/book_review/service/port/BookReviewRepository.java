package com.gathering.book_review.service.port;

import com.gathering.book_review.domain.BookReviewDomain;

public interface BookReviewRepository {

    BookReviewDomain save(BookReviewDomain bookReview);

    BookReviewDomain findById(long reviewId);

    void delete(BookReviewDomain bookReview);

    void decrementLike(Long reviewId);

    void incrementLike(Long reviewId);

}
