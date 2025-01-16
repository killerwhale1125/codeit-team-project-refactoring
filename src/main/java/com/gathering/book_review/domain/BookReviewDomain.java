package com.gathering.book_review.domain;

import com.gathering.book.domain.BookDomain;
import com.gathering.review.domain.StatusType;
import com.gathering.book_review_comment.domain.BookReviewCommentDomain;
import com.gathering.review_like.domain.ReviewLikesDomain;
import com.gathering.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.gathering.review.domain.StatusType.Y;

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
    private List<BookReviewCommentDomain> reviewComments;
    private List<ReviewLikesDomain> reviewLikes;

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
}
