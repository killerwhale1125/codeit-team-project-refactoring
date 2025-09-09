package com.gathering.gathering.util;

import com.gathering.gathering.controller.response.GatheringResponse;
import com.gathering.gathering.controller.response.GatheringResultPageResponse;
import com.gathering.gathering.controller.response.GatheringSearchResponse;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.domain.GatheringWeek;
import com.gathering.gathering.domain.ReadingTimeGoal;
import com.gathering.gathering.infrastructure.entity.Gathering;
import com.gathering.gathering.service.dto.GatheringPageResponse;
import com.gathering.gathering.service.dto.GatheringSliceResponse;

import java.util.Date;
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

    public static GatheringSearchResponse convertToMyGatheringPage(GatheringPageResponse gatheringPageResponse, Map<Long, Double> challengeReadingRateMap) {
        List<GatheringResponse> gatheringResponses = gatheringPageResponse.getGatherings().stream()
                .map(gathering -> GatheringResponse.myGatheringFromEntity(gathering, challengeReadingRateMap))
                .collect(Collectors.toList());

        return GatheringSearchResponse.fromEntity(gatheringResponses, gatheringPageResponse.getTotalCount());
    }

    public static GatheringSearchResponse convertToGatheringsResultPage(GatheringPageResponse gatherings, long totalElements) {
        /**
         * 검색한 모임 리스트 DTO 변환
         */
        List<GatheringResultPageResponse> gatheringResultPageResponses = gatherings.getObjects().stream()
                .map(GatheringDtoMapper::convertRowToGatheringResultPageResponse)
                .collect(Collectors.toList());

        return GatheringSearchResponse.gatheringsResultPage(gatheringResultPageResponses, totalElements);
    }

    public static GatheringResultPageResponse convertRowToGatheringResultPageResponse(Object[] row) {
        return new GatheringResultPageResponse(
                (Long) row[0],  // GATHERING_ID
                (String) row[1], // NAME
                (Integer) row[2], // CURRENT_CAPACITY
                (Integer) row[3], // MAX_CAPACITY
                GatheringWeek.valueOf((String) row[4]).getWeek(), // GATHERING_WEEK
                GatheringStatus.valueOf((String) row[5]),
                (Date) row[6],
                ReadingTimeGoal.valueOf((String) row[7]).getMinutes(), // READING_TIME_GOAL
                (String) row[8], // IMAGE_URL
                (Long) row[9], // BOOK_ID
                (String) row[10],  // BOOK TITLE
                (String) row[11]   // BOOK IMAGE
        );
    }
}
