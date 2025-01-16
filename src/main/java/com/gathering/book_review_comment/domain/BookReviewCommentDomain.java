package com.gathering.book_review_comment.domain;

import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.review.domain.StatusType;
import com.gathering.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BookReviewCommentDomain {
    private long id;
    private UserDomain user;
    private BookReviewDomain review;
    private String content;
    private long parent;
    private int orders;
    private StatusType status;
    private List<BookReviewCommentDomain> replies;

}
