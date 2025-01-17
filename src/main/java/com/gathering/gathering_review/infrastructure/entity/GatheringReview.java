package com.gathering.gathering_review.infrastructure.entity;

import com.gathering.book_review.domain.StatusType;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.infrastructure.entity.Gathering;
import com.gathering.gathering_review.domain.GatheringReviewDomain;
import com.gathering.user.infrastructure.entitiy.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GatheringReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gathering_review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    private String content;
    private int score;

    public static GatheringReview fromEntity(GatheringReviewDomain gatheringReview) {
        GatheringReview gatheringReviewEntity = new GatheringReview();
        gatheringReviewEntity.id =gatheringReview.getId();
        gatheringReviewEntity.content = gatheringReview.getContent();
        gatheringReviewEntity.score = gatheringReview.getScore();
        gatheringReviewEntity.status = StatusType.Y;
        gatheringReviewEntity.user = nullableEntity(User::fromEntity, gatheringReview.getUser());

        Gathering gathering = nullableEntity(Gathering::fromEntity, gatheringReview.getGathering());
        if(gathering != null) {
            gatheringReviewEntity.gathering = gathering;
            gathering.getGatheringReviews().add(gatheringReviewEntity);
        }

        return gatheringReviewEntity;
    }

    public GatheringReviewDomain toEntity() {
        GatheringReviewDomain.GatheringReviewDomainBuilder builder = GatheringReviewDomain.builder()
                .id(id)
                .content(content)
                .score(score)
                .status(status);

        if (user != null && getPersistenceUtil().isLoaded(user)) {
            builder.user(user.toEntity());
        }

        return builder.build();
    }
}
