package com.gathering.book_review_comment.domain;

import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.common.base.exception.BaseException;
import com.gathering.book_review.domain.StatusType;
import com.gathering.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static com.gathering.common.base.response.BaseResponseStatus.REVIEW_OWNER_MISMATCH;

@Getter
@Builder
public class BookReviewCommentDomain {
    private Long id;
    private UserDomain user;
    private BookReviewDomain review;
    private String content;
    private long parent;
    private int orders;
    private StatusType status;
    private List<BookReviewCommentDomain> replies;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public static BookReviewCommentDomain create(BookReviewCommentCreate bookReviewCommentCreate, UserDomain user, BookReviewDomain bookReview) {
        return BookReviewCommentDomain.builder()
                .user(user)
                .review(bookReview)
                .content(bookReviewCommentCreate.getContent())
                .parent(bookReviewCommentCreate.getParent())
                .status(StatusType.Y)
                .build();
    }

    public BookReviewCommentDomain delete(UserDomain user, BookReviewCommentDomain bookReviewComment) {
        validateCreatorOrThrow(user.getUserName());

        this.status = StatusType.N;
        return this;
    }

    private void validateCreatorOrThrow(String username) {
        if (this.user.getUserName().equals(username)) {
            throw new BaseException(REVIEW_OWNER_MISMATCH);
        }
    }

    public BookReviewCommentDomain update(BookReviewCommentUpdate bookReviewCommentUpdate, UserDomain user) {
        validateCreatorOrThrow(user.getUserName());
        this.content = bookReviewCommentUpdate.getContent();
        return this;
    }
}
