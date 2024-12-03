package com.gathering.gathering.repository;

import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.entity.Gathering;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GatheringSearchRepository {
    List<Gathering> findGatherings(GatheringSearch gatheringSearch, Pageable pageable);
}
