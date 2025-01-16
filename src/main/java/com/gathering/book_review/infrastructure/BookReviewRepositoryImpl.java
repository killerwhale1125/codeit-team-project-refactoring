package com.gathering.book_review.infrastructure;

import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.book_review.infrastructure.entity.BookReview;
import com.gathering.book_review.service.port.BookReviewRepository;
import com.gathering.common.base.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.gathering.common.base.response.BaseResponseStatus.NON_EXISTED_REVIEW;

@Repository
@RequiredArgsConstructor
public class BookReviewRepositoryImpl implements BookReviewRepository {

    private final BookReviewJpaRepository bookReviewJpaRepository;

    @Override
    public BookReviewDomain save(BookReviewDomain bookReview) {
        return bookReviewJpaRepository.save(BookReview.fromEntity(bookReview)).toEntity();
    }

    @Override
    public BookReviewDomain findById(long reviewId) {
        return bookReviewJpaRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(NON_EXISTED_REVIEW)).toEntity();
    }
}
