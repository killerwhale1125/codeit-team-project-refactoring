package com.gathering.book_review_comment.infrastructure;

import com.gathering.book_review_comment.domain.BookReviewCommentDomain;
import com.gathering.book_review_comment.infrastructure.entity.BookReviewComment;
import com.gathering.book_review_comment.service.port.BookReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookReviewCommentRepositoryImpl implements BookReviewCommentRepository {

    private final BookReviewCommentJpaRepository bookReviewCommentJpaRepository;

    @Override
    public BookReviewCommentDomain save(BookReviewCommentDomain bookReviewCommentDomain) {
        return bookReviewCommentJpaRepository.save(BookReviewComment.fromEntity(bookReviewCommentDomain)).toEntity();
    }
}
