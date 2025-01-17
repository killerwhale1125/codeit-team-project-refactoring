package com.gathering.book_review_like.infrastructure;

import com.gathering.book_review_like.domain.BookReviewLikeDomain;
import com.gathering.book_review_like.infrastructure.entity.BookReviewLike;
import com.gathering.book_review_like.service.port.BookReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookReviewLikeRepositoryImpl implements BookReviewLikeRepository {

    private final BookReviewLikeJpaRepository bookReviewLikeJpaRepository;

    @Override
    public BookReviewLikeDomain findByReviewIdAndUserIdOrElse(Long reviewId, Long userId) {
        return bookReviewLikeJpaRepository.findByReviewIdAndUserId(reviewId, userId)
                .map(BookReviewLike::toEntity)
                .orElse(null);
    }

    @Override
    public void deleteById(Long bookReviewLikeId) {
        bookReviewLikeJpaRepository.deleteById(bookReviewLikeId);
    }

    @Override
    public BookReviewLikeDomain save(BookReviewLikeDomain bookReviewLike) {
        return bookReviewLikeJpaRepository.save(BookReviewLike.fromEntity(bookReviewLike)).toEntity();
    }
}
