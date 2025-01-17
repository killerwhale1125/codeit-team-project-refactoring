package com.gathering.gathering_review.service;

import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.service.port.GatheringRepository;
import com.gathering.gathering_review.controller.port.GatheringReviewService;
import com.gathering.gathering_review.controller.response.GatheringReviewResponse;
import com.gathering.gathering_review.domain.GatheringReviewCreate;
import com.gathering.gathering_review.domain.GatheringReviewDomain;
import com.gathering.gathering_review.domain.GatheringReviewUpdate;
import com.gathering.gathering_review.service.port.GatheringReviewRepository;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.service.port.UserRepository;
import com.gathering.util.date.DateHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GatheringReviewServiceImpl implements GatheringReviewService {

    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;
    private final GatheringReviewRepository gatheringReviewRepository;
    private final DateHolder dateHolder;

    @Override
    @Transactional
    public GatheringReviewResponse create(String username, GatheringReviewCreate gatheringReviewCreate) {
        UserDomain user = userRepository.findByUsername(username);
        GatheringDomain gathering = gatheringRepository.getById(gatheringReviewCreate.getGatheringId());

        GatheringReviewDomain gatheringReview = gatheringReviewRepository.save(GatheringReviewDomain.create(gatheringReviewCreate, gathering, user));

        return GatheringReviewResponse.fromEntity(gatheringReview);

    }

    @Override
    public void delete(long reviewId, String username) {
        GatheringReviewDomain gatheringReview = gatheringReviewRepository.findByIdWithUser(reviewId);
        gatheringReview = gatheringReview.delete(username);
        gatheringReviewRepository.delete(gatheringReview);
    }

    @Override
    public void update(GatheringReviewUpdate gatheringReviewUpdate, Long reviewId, String username) {
        GatheringReviewDomain gatheringReview = gatheringReviewRepository.findByIdWithUser(reviewId);
        gatheringReview = gatheringReview.update(gatheringReviewUpdate, username, dateHolder);
        gatheringReviewRepository.update(gatheringReview);
    }
}
