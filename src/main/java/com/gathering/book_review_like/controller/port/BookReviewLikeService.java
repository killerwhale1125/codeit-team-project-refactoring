package com.gathering.book_review_like.controller.port;

import com.gathering.book_review_like.domain.BookReviewLikeCreate;

public interface BookReviewLikeService {
    void like(BookReviewLikeCreate bookReviewLikeCreate, String username);
}
