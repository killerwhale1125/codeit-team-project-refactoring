package com.gathering.book_review.infrastructure;

import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.book_review.infrastructure.entity.BookReview;
import com.gathering.book_review.service.port.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookReviewRepositoryImpl implements BookReviewRepository {

    private final BookReviewJpaRepository bookReviewJpaRepository;

    @Override
    public BookReviewDomain save(BookReviewDomain bookReview) {
        return bookReviewJpaRepository.save(BookReview.fromEntity(bookReview)).toEntity();
    }
}
