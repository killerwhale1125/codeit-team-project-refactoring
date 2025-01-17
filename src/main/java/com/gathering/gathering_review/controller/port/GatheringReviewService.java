package com.gathering.gathering_review.controller.port;

import com.gathering.gathering_review.controller.response.GatheringReviewResponse;
import com.gathering.gathering_review.domain.GatheringReviewCreate;
import com.gathering.gathering_review.domain.GatheringReviewUpdate;

public interface GatheringReviewService {
    GatheringReviewResponse create(String username, GatheringReviewCreate gatheringReviewCreate);

    void delete(long reviewId, String username);

    void update(GatheringReviewUpdate gatheringReviewUpdate, Long reviewId, String username);
}
