package com.gathering.gathering_review.service.port;

import com.gathering.gathering_review.domain.GatheringReviewDomain;

public interface GatheringReviewRepository {
    GatheringReviewDomain save(GatheringReviewDomain gatheringReviewDomain);

    GatheringReviewDomain findByIdWithUser(long reviewId);

    void delete(GatheringReviewDomain gatheringReview);

    GatheringReviewDomain findById(Long reviewId);

    void update(GatheringReviewDomain gatheringReview);

}
