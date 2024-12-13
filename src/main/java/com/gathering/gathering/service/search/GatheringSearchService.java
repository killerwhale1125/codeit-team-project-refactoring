package com.gathering.gathering.service.search;

import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.model.entity.GatheringReviewSortType;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.review.model.dto.ReviewListDto;
import org.springframework.data.domain.Pageable;

public interface GatheringSearchService {
    GatheringSearchResponse findGatherings(GatheringSearch gatheringSearch, Pageable pageable);

    GatheringResponse getById(Long gatheringId, String userKey);

    GatheringSearchResponse findMyGatherings(String username, Pageable pageable, GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus);

    GatheringSearchResponse findMyCreated(String username, Pageable pageable);

    GatheringSearchResponse findMyWishes(String username, Pageable pageable);

    GatheringResponse introduce(Long gatheringId);

    ReviewListDto review(Long gatheringId, GatheringReviewSortType sort, Pageable pageable);
}
