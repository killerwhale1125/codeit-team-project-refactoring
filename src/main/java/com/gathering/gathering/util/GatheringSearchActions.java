package com.gathering.gathering.util;

import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringResultPage;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.model.entity.Gathering;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public GatheringSearchResponse convertToMyGatheringPage(Page<Gathering> result) {
        List<GatheringResponse> gatheringResponses = result.getContent().stream()
                .map(GatheringResponse::myGatheringFromEntity)
                .collect(Collectors.toList());

        return GatheringSearchResponse.fromEntity(gatheringResponses, result.getTotalElements());
    }

    public Page<GatheringResultPage> convertToResultPage(Page<Tuple> result, Pageable pageable) {

        List<Tuple> content = result.getContent();
//        List<GatheringResultPage> resultPages = result.getContent().stream()
//                .map(tuple -> new GatheringResultPage(
//                        Long.parseLong(tuple.get("GATHERING_ID", Long.class)),
//                        tuple.get("NAME", String.class),
//                        tuple.get("THUMBNAIL", String.class),
//                        tuple.get("GOAL_DAYS", Long.class),
//                        tuple.get("READING_TIME_GOAL", Integer.class),
//                        tuple.get("MAX_CAPACITY", Integer.class),
//                        tuple.get("CURRENT_CAPACITY", Integer.class),
//                        tuple.get("TITLE", String.class),
//                        tuple.get("IMAGE", String.class)
//                ))
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(resultPages, pageable, result.getTotalElements());
        return null;
    }
}
