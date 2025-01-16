package com.gathering.book_review.controller.port;

import com.gathering.book_review.controller.response.BookReviewResponse;
import com.gathering.book_review.domain.BookReviewCreate;

public interface BookReviewService {
    BookReviewResponse create(String username, BookReviewCreate bookReviewCreate);
}
