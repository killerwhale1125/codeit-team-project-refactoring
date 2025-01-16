package com.gathering.gathering_review.domain;

import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.review.domain.StatusType;
import com.gathering.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GatheringReviewDomain {
    private Long id;
    private UserDomain user;
    private GatheringDomain gathering;
    private String content;
    private int score;
    private StatusType status;

    public static GatheringReviewDomain create(GatheringReviewCreate gatheringReviewCreate, GatheringDomain gathering, UserDomain user) {
        return GatheringReviewDomain.builder()
                .user(user)
                .gathering(gathering)
                .content(gatheringReviewCreate.getContent())
                .score(gatheringReviewCreate.getScore())
                .status(StatusType.Y)
                .build();
    }
}
