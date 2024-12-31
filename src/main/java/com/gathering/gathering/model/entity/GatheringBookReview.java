package com.gathering.gathering.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.review.model.constant.StatusType;
import com.gathering.review.model.entitiy.BookReview;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GatheringBookReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gathering_review_id")
    @Comment("모임 리뷰 pk")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id", nullable = true)
    @Comment("모임 ID")
    private Gathering gathering;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @Comment("리뷰 ID")
    private BookReview review;

    @Comment("상태")
    @Enumerated(EnumType.STRING)
    private StatusType status;

    public static GatheringBookReview createGatheringReview(Gathering gathering, BookReview review) {

        return GatheringBookReview.builder()
                .gathering(gathering)
                .review(review)
                .status(StatusType.Y)
                .build();
    }

    public void addGatheringReview(Gathering gathering) {
        this.gathering = gathering;
        gathering.getGatheringBookReviews().add(this);
    }

}
