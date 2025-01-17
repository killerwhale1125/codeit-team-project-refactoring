package com.gathering.book_review.domain;

import com.gathering.book.domain.BookDomain;
import com.gathering.book_review_comment.domain.BookReviewCommentDomain;
import com.gathering.common.base.exception.BaseException;
import com.gathering.book_review_like.domain.BookReviewLikeDomain;
import com.gathering.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static com.gathering.common.base.response.BaseResponseStatus.REVIEW_OWNER_MISMATCH;
import static com.gathering.book_review.domain.StatusType.Y;

@Getter
@Builder
public class BookReviewDomain {
    private Long id;
    private UserDomain user;
    private BookDomain book;
    private String title;
    private String apprCd;
    private String tagCd;
    private String content;
    private int likes;
    private StatusType status;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private List<BookReviewCommentDomain> reviewComments;
    private List<BookReviewLikeDomain> reviewLikes;

    public static BookReviewDomain create(BookReviewCreate bookReviewCreate, BookDomain book, UserDomain user) {
        return BookReviewDomain.builder()
                .user(user)
                .book(book)
                .title(bookReviewCreate.getTitle())
                .apprCd(bookReviewCreate.getApprCd())
                .tagCd(bookReviewCreate.getTag())
                .content(bookReviewCreate.getContent())
                .likes(0)
                .status(bookReviewCreate.getTmprStrgYN().equals(Y) ? StatusType.T : StatusType.Y)   // T = 임시저장, Y= '저장' , N = 삭제'
                .build();
    }

    public BookReviewDomain delete(String username) {
        validateCreatorOrThrow(username);
        this.status = StatusType.N;
        return this;
    }

    public BookReviewDomain update(BookReviewUpdate bookReviewUpdate, UserDomain user, BookDomain book) {
        validateCreatorOrThrow(user.getUserName());
        this.book = book;
        this.user = user;
        this.title = bookReviewUpdate.getTitle();
        this.apprCd = bookReviewUpdate.getApprCd();
        this.tagCd = bookReviewUpdate.getTag();
        this.content = bookReviewUpdate.getContent();
        return this;
    }

    private void validateCreatorOrThrow(String username) {
        if (this.user.getUserName().equals(username)) {
            throw new BaseException(REVIEW_OWNER_MISMATCH);
        }
    }
}
