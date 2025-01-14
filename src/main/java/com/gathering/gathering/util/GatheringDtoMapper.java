package com.gathering.gathering.util;

import com.gathering.gathering.controller.response.GatheringResponse;
import com.gathering.gathering.controller.response.GatheringSearchResponse;
import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.infrastructure.entity.Gathering;
import com.gathering.gathering.service.dto.GatheringSliceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GatheringDtoMapper {
    public static GatheringSearchResponse convertToGatheringSearchResponse(GatheringSliceResponse gatheringSliceResponse, Set<Long> wishGatheringIds) {
        List<GatheringResponse> gatheringResponses = gatheringSliceResponse.getGatherings().stream()
                .map(gathering -> GatheringResponse.from(gathering, wishGatheringIds.contains(gathering.getId())))
                .collect(Collectors.toList());
        boolean hasNext = gatheringSliceResponse.isHasNext();
        return GatheringSearchResponse.fromEntity(gatheringResponses, hasNext);
    }

    public static GatheringSearchResponse convertToGatheringSearchJoinableResponse(GatheringSliceResponse gatheringSliceResponse, Set<Long> wishGatheringIds) {
        List<GatheringResponse> gatheringResponses = gatheringSliceResponse.getGatherings().stream()
                .map(gathering -> GatheringResponse.joinableGatherings(gathering, wishGatheringIds.contains(gathering.getId())))
                .collect(Collectors.toList());

        boolean hasNext = gatheringSliceResponse.isHasNext();

        return GatheringSearchResponse.fromEntity(gatheringResponses, hasNext);
    }

    public static GatheringSearchResponse convertToMyGatheringPage(Page<Gathering> result, Map<Long, Double> challengeReadingRateMap) {
        List<GatheringResponse> gatheringResponses = result.getContent().stream()
                .map(gathering -> GatheringResponse.myGatheringFromEntity(gathering, challengeReadingRateMap))
                .collect(Collectors.toList());

        return GatheringSearchResponse.fromEntity(gatheringResponses, result.getTotalElements());
    }
}
