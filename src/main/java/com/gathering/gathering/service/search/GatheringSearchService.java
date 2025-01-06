package com.gathering.gathering.service.search;

import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.model.entity.GatheringReviewSortType;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.model.entity.SearchType;
import com.gathering.review.model.dto.ReviewListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface GatheringSearchService {
    GatheringSearchResponse findGatherings(GatheringSearch gatheringSearch, Pageable pageable, UserDetails userDetails);

    GatheringResponse getById(Long gatheringId, String userKey, UserDetails userDetails);

    GatheringSearchResponse findMyGatherings(String username, Pageable pageable, GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus);

    GatheringSearchResponse findMyCreated(String username, Pageable pageable);

    GatheringSearchResponse findMyWishes(String username, Pageable pageable);

    GatheringResponse introduce(Long gatheringId);

    ReviewListDto review(Long gatheringId, GatheringReviewSortType sort, Pageable pageable);

    GatheringSearchResponse getIntegratedResultBySearchWordAndType(String searchWord, SearchType searchType, Pageable pageable, String username);

    GatheringSearchResponse getGatheringsBySearchWordAndType(String searchWord, SearchType searchType, Pageable pageable);

    GatheringSearchResponse getReviewsBySearchWordAndType(String searchWord, SearchType searchType, Pageable pageable, String username);

    GatheringSearchResponse findJoinableGatherings(GatheringSearch gatheringSearch, Pageable pageable, UserDetails userDetails);
}
