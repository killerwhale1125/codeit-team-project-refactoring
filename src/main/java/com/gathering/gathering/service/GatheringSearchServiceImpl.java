package com.gathering.gathering.service;

import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.repository.GatheringSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GatheringSearchServiceImpl implements GatheringSearchService {

    private final GatheringSearchRepository gatheringSearchRepository;

    @Override
    public List<GatheringResponse> findGatherings(GatheringSearch gatheringSearch) {
        Pageable pageable = PageRequest.of(gatheringSearch.getPage(), gatheringSearch.getSize());
        gatheringSearchRepository.findGatherings(gatheringSearch, pageable);
        return null;
    }
}
