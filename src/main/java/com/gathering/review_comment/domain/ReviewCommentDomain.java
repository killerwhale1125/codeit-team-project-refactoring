package com.gathering.review_comment.domain;

import com.gathering.book_review.domain.BookReviewDomain;
import com.gathering.review.domain.StatusType;
import com.gathering.user.infrastructure.entitiy.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewCommentDomain {
    private long id;
    private User user;
    private BookReviewDomain review;
    private String content;
    private long parent;
    private int orders;
    private StatusType status;
}
