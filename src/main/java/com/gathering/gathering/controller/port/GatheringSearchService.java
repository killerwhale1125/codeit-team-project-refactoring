package com.gathering.gathering.controller.port;

import com.gathering.gathering.controller.response.GatheringResponse;
import com.gathering.gathering.controller.response.GatheringSearchResponse;
import com.gathering.gathering.domain.GatheringReviewSortType;
import com.gathering.gathering.domain.GatheringSearch;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.domain.SearchType;
import com.gathering.gatheringuser.domain.GatheringUserStatus;
import com.gathering.review.model.dto.ReviewListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface GatheringSearchService {
    GatheringSearchResponse findGatheringsByFilters(GatheringSearch gatheringSearch, int page, int size, UserDetails userDetails);

    GatheringResponse getById(Long gatheringId, String userKey, UserDetails userDetails);

    GatheringSearchResponse findMyGatherings(String username, int page, int size, GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus);

    GatheringSearchResponse findMyCreated(String username, int page, int size);

    GatheringSearchResponse findMyWishes(String username, int page, int size);

    GatheringResponse introduce(Long gatheringId);

    ReviewListDto review(Long gatheringId, GatheringReviewSortType sort, int page, int size);

    GatheringSearchResponse getIntegratedResultBySearchWordAndType(String searchWord, SearchType searchType, int page, int size, String username);

    GatheringSearchResponse getGatheringsBySearchWordAndType(String searchWord, SearchType searchType, int page, int size);

    GatheringSearchResponse getReviewsBySearchWordAndType(String searchWord, SearchType searchType, int page, int size, String username);

    GatheringSearchResponse findJoinableGatherings(GatheringSearch gatheringSearch, int page, int size, UserDetails userDetails);
}
