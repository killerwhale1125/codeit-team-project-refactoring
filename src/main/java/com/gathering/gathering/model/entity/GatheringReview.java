package com.gathering.gathering.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.review.model.entitiy.Review;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GatheringReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gathering_review_id")
    @Comment("모임 리뷰 pk")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    @Comment("모임 ID")
    private Gathering gathering;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @Comment("리뷰 ID")
    private Review review;

    @Comment("상태")
    private String status;

    public static GatheringReview createGatheringReview(Gathering gathering, Review review) {

        return GatheringReview.builder()
                .gathering(gathering)
                .review(review)
                .status("Y")
                .build();
    }

    public void addGatheringReview(Gathering gathering) {
        this.gathering = gathering;
        gathering.getGatheringReviews().add(this);
    }

}
