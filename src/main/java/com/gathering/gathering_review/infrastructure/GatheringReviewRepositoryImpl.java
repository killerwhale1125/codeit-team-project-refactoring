package com.gathering.gathering_review.infrastructure;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering_review.domain.GatheringReviewDomain;
import com.gathering.gathering_review.infrastructure.entity.GatheringReview;
import com.gathering.gathering_review.service.port.GatheringReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.gathering.common.base.response.BaseResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class GatheringReviewRepositoryImpl implements GatheringReviewRepository {

    private final GatheringReviewJpaRepository gatheringReviewJpaRepository;

    @Override
    public GatheringReviewDomain save(GatheringReviewDomain gatheringReview) {
        return gatheringReviewJpaRepository.save(GatheringReview.fromEntity(gatheringReview)).toEntity();
    }

    @Override
    public GatheringReviewDomain findByIdWithUser(long reviewId) {
        return gatheringReviewJpaRepository.findByIdWithUser(reviewId).toEntity();
    }

    @Override
    public void delete(GatheringReviewDomain gatheringReview) {
        gatheringReviewJpaRepository.deleteReview(gatheringReview.getId(), gatheringReview.getStatus());
    }

    @Override
    public GatheringReviewDomain findById(Long reviewId) {
        return gatheringReviewJpaRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(NON_EXISTED_REVIEW))
                .toEntity();
    }

    @Override
    public void update(GatheringReviewDomain gatheringReview) {
        gatheringReviewJpaRepository.update(gatheringReview.getId(), gatheringReview.getContent(), gatheringReview.getScore(), gatheringReview.getModifiedTime());
    }
}
