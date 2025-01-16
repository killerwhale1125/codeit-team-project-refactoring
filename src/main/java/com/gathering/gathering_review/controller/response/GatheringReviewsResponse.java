package com.gathering.gathering_review.controller.response;

import com.gathering.gathering.controller.response.GatheringResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GatheringReviewsResponse {

    private List<GatheringResponse> gatheringResponses;
    private long writableReviewCount;
    private List<GatheringReviewResponse> gatheringReviewResponses;
    private long writedReviewCount;

    public static GatheringReviewsResponse fromGatheringReviews(
            List<GatheringResponse> gatheringResponses, int writableReviewCount,
            List<GatheringReviewResponse> reviews, Long writedReviewCount) {

        return GatheringReviewsResponse.builder()
                .gatheringResponses(gatheringResponses)
                .writableReviewCount(writableReviewCount)
                .gatheringReviewResponses(reviews)
                .writedReviewCount(writedReviewCount)
                .build();
    }
}
