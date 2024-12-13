package com.gathering.review.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.gathering.model.dto.GatheringResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Builder
public class ReviewListDto {


    // 독서 리뷰
    private List<BookReviewDto> bookReviews;

    // 작성 가능한 모임
    private List<GatheringResponse> gatheringResponses;

    // 작성한 리뷰
    private List<GatheringReviewDto> gatheringReviews;

    private long total;
    private double scoreAvg;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private boolean hasNext;

    public static ReviewListDto fromBookReviews(List<BookReviewDto> bookReview) {
        return ReviewListDto.builder()
                .bookReviews(bookReview)
                .build();
    }

    public static ReviewListDto fromGatheringResponses(List<GatheringResponse> gatheringResponses) {
        return ReviewListDto.builder()
                .gatheringResponses(gatheringResponses)
                .build();
    }

    public static ReviewListDto fromGatheringReviews(List<GatheringResponse> gatheringResponses, List<GatheringReviewDto> gatheringReviews) {
        return ReviewListDto.builder()
                .gatheringResponses(gatheringResponses)
                .gatheringReviews(gatheringReviews)
                .build();
    }


    public static ReviewListDto fromGatheringReviews(List<GatheringReviewDto> gatheringReviews, long total, double scoreAvg, boolean hasNext) {
        return ReviewListDto.builder()
                .gatheringReviews(gatheringReviews)
                .total(total)
                .scoreAvg(scoreAvg)
                .hasNext(hasNext)
                .build();
    }
}
