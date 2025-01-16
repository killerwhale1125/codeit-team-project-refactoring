package com.gathering.gathering_review.service.port;

import com.gathering.gathering_review.controller.response.GatheringReviewsResponse;
import com.gathering.user.domain.UserDomain;

public interface GatheringReviewSearchRepository {
    GatheringReviewsResponse getMyReviews(UserDomain user, int page, int size);
}
