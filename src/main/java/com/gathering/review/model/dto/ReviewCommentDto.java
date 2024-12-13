package com.gathering.review.model.dto;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.review.model.entitiy.BookReview;
import com.gathering.review.model.entitiy.ReviewComment;
import com.gathering.user.model.entitiy.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ReviewCommentDto{

    private long id;
    private long userId;
    private long reviewId;
    private String content;
    private long parent;
    private int orders;
    private String status;

    public static ReviewCommentDto formEntity(ReviewComment comment) {
        return ReviewCommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .reviewId(comment.getReview().getId())
                .content(comment.getContent())
                .parent(comment.getParent())
                .orders(comment.getOrders())
                .status(comment.getStatus())
                .build();
    }
}
