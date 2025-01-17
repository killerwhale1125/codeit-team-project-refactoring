package com.gathering.book_review_comment.infrastructure;

import com.gathering.book_review_comment.domain.BookReviewCommentDomain;
import com.gathering.book_review_comment.infrastructure.entity.BookReviewComment;
import com.gathering.book_review_comment.service.port.BookReviewCommentRepository;
import com.gathering.common.base.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.gathering.common.base.response.BaseResponseStatus.NON_EXISTED_COMMENT;

@Repository
@RequiredArgsConstructor
public class BookReviewCommentRepositoryImpl implements BookReviewCommentRepository {

    private final BookReviewCommentJpaRepository bookReviewCommentJpaRepository;

    @Override
    public BookReviewCommentDomain save(BookReviewCommentDomain bookReviewCommentDomain) {
        return bookReviewCommentJpaRepository.save(BookReviewComment.fromEntity(bookReviewCommentDomain)).toEntity();
    }

    @Override
    public BookReviewCommentDomain findByIdWithUser(Long commentId) {
        return bookReviewCommentJpaRepository.findByIdWithUser(commentId).toEntity();
    }

    @Override
    public void delete(BookReviewCommentDomain bookReviewComment) {
        bookReviewCommentJpaRepository.deleteComment(bookReviewComment.getId(), bookReviewComment.getStatus());
    }

    @Override
    public BookReviewCommentDomain findById(long commentId) {
        return bookReviewCommentJpaRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(NON_EXISTED_COMMENT)).toEntity();
    }

    @Override
    public void update(BookReviewCommentDomain bookReviewComment) {
        bookReviewCommentJpaRepository.update(
                bookReviewComment.getId(),
                bookReviewComment.getContent());
    }
}
