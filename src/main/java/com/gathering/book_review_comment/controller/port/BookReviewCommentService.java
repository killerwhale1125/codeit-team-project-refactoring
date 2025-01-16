package com.gathering.book_review_comment.controller.port;

import com.gathering.book_review_comment.controller.response.BookReviewCommentResponse;
import com.gathering.book_review_comment.domain.BookReviewCommentCreate;

public interface BookReviewCommentService {
    BookReviewCommentResponse create(String username, BookReviewCommentCreate bookReviewCommentCreate);
}
