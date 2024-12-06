package com.gathering.gathering.service.search;

import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import org.springframework.data.domain.Pageable;

public interface GatheringSearchService {
    GatheringSearchResponse findGatherings(GatheringSearch gatheringSearch, Pageable pageable);

    GatheringResponse getById(Long gatheringId, String userKey);
}
