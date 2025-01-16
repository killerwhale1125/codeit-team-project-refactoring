package com.gathering.gathering_review.controller.port;

import com.gathering.gathering_review.controller.response.GatheringReviewsResponse;

public interface GatheringReviewSearchService {
    GatheringReviewsResponse myReviews(String username, int page, int size);
}
