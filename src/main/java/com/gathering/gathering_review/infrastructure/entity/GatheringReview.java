package com.gathering.gathering_review.infrastructure.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.infrastructure.entity.Gathering;
import com.gathering.gathering_review.domain.GatheringReviewDomain;
import com.gathering.review.domain.StatusType;
import com.gathering.review.model.dto.CreateReviewDto;
import com.gathering.user.infrastructure.entitiy.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

import static com.gathering.util.entity.EntityUtils.nullableEntity;
import static jakarta.persistence.Persistence.getPersistenceUtil;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GatheringReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    @Comment("리뷰 pk")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Comment("작성자")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    @Comment("모임")
    private Gathering gathering;

    @Comment("내용")
    private String content;

    @Comment("점수")
    private int score;

    @Comment("상태")
    @Enumerated(EnumType.STRING)
    private StatusType status;

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

    public void updateReview(CreateReviewDto dto) {
        this.content = dto.getContent();
        this.score = dto.getScore();
        this.modifiedTime = LocalDateTime.now();
    }

    public static GatheringReview createEntity(Gathering gathering, User user, CreateReviewDto createReviewDto) {
        return GatheringReview.builder()
                .user(user)
                .gathering(gathering)
                .content(createReviewDto.getContent())
                .score(createReviewDto.getScore())
                .status(StatusType.Y)
                .build();
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
