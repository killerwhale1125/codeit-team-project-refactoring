package com.gathering.gathering_review.service;

import com.gathering.gathering_review.controller.port.GatheringReviewSearchService;
import com.gathering.gathering_review.controller.response.GatheringReviewsResponse;
import com.gathering.gathering_review.service.port.GatheringReviewSearchRepository;
import com.gathering.user.domain.UserDomain;
import com.gathering.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GatheringReviewSearchServiceImpl implements GatheringReviewSearchService {

    private final GatheringReviewSearchRepository gatheringReviewSearchRepository;
    private final UserRepository userRepository;

    @Override
    public GatheringReviewsResponse myReviews(String username, int page, int size) {
        UserDomain user = userRepository.findByUsername(username);
        return gatheringReviewSearchRepository.getMyReviews(user, page, size);
    }
}
