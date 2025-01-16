package com.gathering.book_review.domain;

import com.gathering.book.infrastructure.entity.Book;
import com.gathering.review.domain.StatusType;
import com.gathering.review_comment.domain.ReviewCommentDomain;
import com.gathering.review_like.domain.ReviewLikesDomain;
import com.gathering.user.infrastructure.entitiy.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BookReviewDomain {
    private long id;
    private User user;
    private Book book;
    private String title;
    private String apprCd;
    private String tagCd;
    private String content;
    private int likes;
    private StatusType status;
    private List<ReviewCommentDomain> reviewComments;
    private List<ReviewLikesDomain> reviewLikes;
}
