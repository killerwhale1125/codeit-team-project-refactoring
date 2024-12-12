package com.gathering.gathering.util;

import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.model.entity.Gathering;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatheringSearchActions {

    public GatheringSearchResponse convertToGatheringSearchResponse(Slice<Gathering> slice) {
        List<GatheringResponse> gatheringResponses = slice.getContent().stream()
                .map(GatheringResponse::fromEntity)
                .collect(Collectors.toList());

        boolean hasNext = slice.hasNext();

        return GatheringSearchResponse.fromEntity(gatheringResponses, hasNext);
    }

    public GatheringSearchResponse getMyGatheringPage(Page<Gathering> result) {
        List<GatheringResponse> gatheringResponses = result.getContent().stream()
                .map(GatheringResponse::myGatheringFromEntity)
                .collect(Collectors.toList());

        return GatheringSearchResponse.myGatheringsFromEntity(gatheringResponses, result.getTotalElements());
    }
}
