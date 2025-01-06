package com.gathering.review.model.entitiy;

import com.gathering.user.model.entitiy.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "REVIEW_LIKES",
        uniqueConstraints={
                @UniqueConstraint(
                        name="REVIEW_LIKES_UK",
                        columnNames={"USER_ID", "REVIEW_ID"}
                )
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class ReviewLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_likes_id")
    @Comment("좋아요 pk")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("사용자")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @Comment("리뷰 아이디")
    private BookReview review;

    public static ReviewLikes createEntity(BookReview bookReview, User user) {
        return ReviewLikes.builder()
                .user(user)
                .review(bookReview)
                .build();
    }

}
