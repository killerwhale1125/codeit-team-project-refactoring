package com.gathering.gathering_review.controller.response;

import com.gathering.gathering_review.domain.GatheringReviewDomain;
import com.gathering.review.domain.StatusType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GatheringReviewResponse {
    private long id;
    private long userId;
    private long gatheringId;
    private String content;
    private int score;
    @Enumerated(EnumType.STRING)
    private StatusType status;
    private String createDate;
    private String userName;
    private String profile;

    public static GatheringReviewResponse fromEntity(GatheringReviewDomain gatheringReview) {
        return GatheringReviewResponse.builder()
                .id(gatheringReview.getId())
                .userId(gatheringReview.getUser().getId())
                .gatheringId(gatheringReview.getGathering().getId())
                .content(gatheringReview.getContent())
                .score(gatheringReview.getScore())
                .status(gatheringReview.getStatus())
                .build();
    }
}
