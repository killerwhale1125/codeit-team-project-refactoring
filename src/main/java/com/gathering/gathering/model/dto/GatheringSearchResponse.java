package com.gathering.gathering.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GatheringSearchResponse {

    private List<GatheringResponse> gatheringResponses;
    private boolean hasNext;    // 다음 페이지 데이터 존재 여부

    public static GatheringSearchResponse fromEntity(List<GatheringResponse> gatheringResponses, boolean hasNext) {
        return GatheringSearchResponse.builder()
                .gatheringResponses(gatheringResponses)
                .hasNext(hasNext)
                .build();
    }
}
