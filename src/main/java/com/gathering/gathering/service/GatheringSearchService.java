package com.gathering.gathering.service;

import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringSearch;

import java.util.List;

public interface GatheringSearchService {
    List<GatheringResponse> findGatherings(GatheringSearch gatheringSearch);
}
