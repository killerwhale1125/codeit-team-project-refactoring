package com.gathering.gathering.service.search;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.repository.search.GatheringSearchJpaRepository;
import com.gathering.gathering.repository.search.GatheringSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatheringSearchServiceImpl implements GatheringSearchService {

    private final GatheringSearchJpaRepository gatheringSearchJpaRepository;

    @Override
    public GatheringSearchResponse findGatherings(GatheringSearch gatheringSearch, Pageable pageable) {
        Slice<Gathering> slice = gatheringSearchJpaRepository.findGatherings(gatheringSearch, pageable);
        List<GatheringResponse> gatheringResponses = slice.getContent().stream()
                .map(GatheringResponse::fromEntity)
                .collect(Collectors.toList());
        boolean hasNext = slice.hasNext();

        return GatheringSearchResponse.fromEntity(gatheringResponses, hasNext);
    }

    @Override
    public GatheringResponse getById(Long gatheringId) {
        return GatheringResponse.fromEntity(gatheringSearchJpaRepository.findByIdWithBooks(gatheringId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXISTED_GATHERING)));
    }
}
