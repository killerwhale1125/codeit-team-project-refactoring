package com.gathering.gathering_review.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GatheringReviewCreate {
    private long bookId;
    private long gatheringId;
    private String title;
    private String apprCd; // 평가
    private String tag;
    @NotEmpty
    private String content;
    private int score;
    private String tmprStrgYN; // 임시저장 여부
}
