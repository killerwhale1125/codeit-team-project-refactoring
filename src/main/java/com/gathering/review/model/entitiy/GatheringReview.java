package com.gathering.review.model.entitiy;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.infrastructure.entity.Gathering;
import com.gathering.review.model.constant.StatusType;
import com.gathering.review.model.dto.CreateReviewDto;
import com.gathering.user.model.entitiy.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

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
    private long id;

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
}
