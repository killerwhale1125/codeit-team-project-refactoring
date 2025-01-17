package com.gathering.gathering.controller.port;

import com.gathering.gathering.controller.response.GatheringResponse;
import com.gathering.gathering.controller.response.GatheringSearchResponse;
import com.gathering.gathering.domain.GatheringSearch;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.domain.SearchType;
import com.gathering.gathering_user.domain.GatheringUserStatus;

public interface GatheringSearchService {
    GatheringSearchResponse findGatheringsByFilters(GatheringSearch gatheringSearch, int page, int size, String username);

    GatheringResponse getById(Long gatheringId, String userKey, String username);

    GatheringSearchResponse findMyGatherings(String username, int page, int size, GatheringStatus gatheringStatus, GatheringUserStatus gatheringUserStatus);

    GatheringSearchResponse findMyCreated(String username, int page, int size);

    GatheringSearchResponse findMyWishes(String username, int page, int size);

    GatheringResponse introduce(Long gatheringId);

    GatheringSearchResponse getGatheringsBySearchWordAndType(String searchWord, SearchType searchType, int page, int size);

    GatheringSearchResponse findJoinableGatherings(GatheringSearch gatheringSearch, int page, int size, String username);
}
