package com.gathering.review_comment.infrastructure.entity;

import com.gathering.book_review.infrastructure.entity.BookReview;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.review.domain.StatusType;
import com.gathering.review.model.dto.CreateReviewCommentDto;
import com.gathering.user.infrastructure.entitiy.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReviewComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_comment_id")
    @Comment("댓글 pk")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("작성자")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @Comment("리뷰")
    private BookReview review;

    @Comment("내용")
    private String content;

    @Comment("계층")
    private long parent;

    @Comment("순서")
    private int orders;

    @Comment("상태")
    @Enumerated(EnumType.STRING)
    private StatusType status;

    public static ReviewComment createEntity(BookReview review, User user
            , CreateReviewCommentDto createReviewCommentDto, int orders) {
        return ReviewComment.builder()
                .user(user)
                .review(review)
                .content(createReviewCommentDto.getContent())
                .parent(createReviewCommentDto.getParent())
                .orders(orders)
                .status(StatusType.Y)
                .build();
    }
}
