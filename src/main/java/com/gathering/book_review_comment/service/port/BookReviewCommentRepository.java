package com.gathering.book_review_comment.service.port;

import com.gathering.book_review_comment.domain.BookReviewCommentDomain;

public interface BookReviewCommentRepository {
    BookReviewCommentDomain save(BookReviewCommentDomain bookReviewCommentDomain);
}
