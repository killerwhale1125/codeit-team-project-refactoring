package com.gathering.gathering_review.infrastructure;

import com.gathering.gathering_review.domain.GatheringReviewDomain;
import com.gathering.gathering_review.infrastructure.entity.GatheringReview;
import com.gathering.gathering_review.service.port.GatheringReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GatheringReviewRepositoryImpl implements GatheringReviewRepository {

    private final GatheringReviewJpaRepository gatheringReviewJpaRepository;

    @Override
    public GatheringReviewDomain save(GatheringReviewDomain gatheringReview) {
        return gatheringReviewJpaRepository.save(GatheringReview.fromEntity(gatheringReview)).toEntity();
    }
}
