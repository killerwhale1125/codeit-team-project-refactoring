package com.gathering.book_review_like.service.port;

import com.gathering.book_review_like.domain.BookReviewLikeDomain;

public interface BookReviewLikeRepository {
    BookReviewLikeDomain findByReviewIdAndUserIdOrElse(Long reviewId, Long id);

    void deleteById(Long bookReviewLikeId);

    BookReviewLikeDomain save(BookReviewLikeDomain bookReviewLikeDomain);

}
