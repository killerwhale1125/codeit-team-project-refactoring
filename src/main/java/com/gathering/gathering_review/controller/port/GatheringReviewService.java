package com.gathering.gathering_review.controller.port;

import com.gathering.gathering_review.controller.response.GatheringReviewResponse;
import com.gathering.gathering_review.domain.GatheringReviewCreate;

public interface GatheringReviewService {
    GatheringReviewResponse create(String username, GatheringReviewCreate gatheringReviewCreate);

}
