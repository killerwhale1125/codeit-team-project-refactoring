package com.gathering.book_review_comment.controller.port;

import com.gathering.book_review_comment.controller.response.BookReviewCommentResponse;
import com.gathering.book_review_comment.domain.BookReviewCommentCreate;
import com.gathering.book_review_comment.domain.BookReviewCommentUpdate;

public interface BookReviewCommentService {
    BookReviewCommentResponse create(String username, BookReviewCommentCreate bookReviewCommentCreate);

    void delete(Long commentId, String username);

    void update(long commentId, String username, BookReviewCommentUpdate bookReviewCommentUpdate);

}
