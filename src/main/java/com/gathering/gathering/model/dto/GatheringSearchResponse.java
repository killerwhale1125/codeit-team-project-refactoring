package com.gathering.gathering.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.review.model.dto.BookReviewDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class GatheringSearchResponse {
    private List<GatheringResponse> gatheringResponses;
    private List<GatheringResultPageResponse> gatheringResultPageResponses;
    private List<BookReviewDto> reviewResultPageResponses;
    private boolean hasNext;    // 다음 페이지 데이터 존재 여부
    private long totalCount;
    private long reviewTotalCount;

    // 무한 스크롤 전용
    public static GatheringSearchResponse fromEntity(List<GatheringResponse> gatheringResponses, boolean hasNext) {
        return GatheringSearchResponse.builder()
                .gatheringResponses(gatheringResponses)
                .hasNext(hasNext)
                .build();
    }

    public static GatheringSearchResponse fromEntity(List<GatheringResponse> gatheringResponses, long totalCount) {
        return GatheringSearchResponse.builder()
                .gatheringResponses(gatheringResponses)
                .totalCount(totalCount)
                .build();
    }

    public static GatheringSearchResponse empty() {
        return GatheringSearchResponse.builder()
                .gatheringResponses(new ArrayList<>())
                .totalCount(0L)
                .build();
    }

    public static GatheringSearchResponse integratedResultPages(List<GatheringResultPageResponse> gatherings, long gatheringCount, List<BookReviewDto> reviews, long reviewCount) {
        return GatheringSearchResponse.builder()
                .gatheringResultPageResponses(gatherings)
                .totalCount(gatheringCount)
                .reviewResultPageResponses(reviews)
                .reviewTotalCount(reviewCount)
                .build();
    }

    public static GatheringSearchResponse gatheringsResultPage(List<GatheringResultPageResponse> gatherings, long totalElements) {
        return GatheringSearchResponse.builder()
                .gatheringResultPageResponses(gatherings)
                .totalCount(totalElements)
                .build();
    }

    public static GatheringSearchResponse reviewsResultPage(List<BookReviewDto> reviews, long reviewCount) {
        return GatheringSearchResponse.builder()
                .reviewResultPageResponses(reviews)
                .reviewTotalCount(reviewCount)
                .build();
    }
}
