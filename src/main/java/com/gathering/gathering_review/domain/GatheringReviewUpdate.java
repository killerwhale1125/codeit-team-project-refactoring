package com.gathering.gathering_review.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GatheringReviewUpdate {
    private long gatheringId;
    @NotEmpty
    private String content;
    private int score;
}
