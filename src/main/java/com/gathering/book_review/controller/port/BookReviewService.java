package com.gathering.book_review.controller.port;

import com.gathering.book_review.controller.response.BookReviewResponse;
import com.gathering.book_review.domain.BookReviewCreate;
import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.book_review.domain.BookReviewUpdate;

public interface BookReviewService {
    BookReviewResponse create(String username, BookReviewCreate bookReviewCreate);

    void delete(long reviewId, String username);

    BookReviewDomain update(BookReviewUpdate bookReviewUpdate, Long reviewId, String username);
}
