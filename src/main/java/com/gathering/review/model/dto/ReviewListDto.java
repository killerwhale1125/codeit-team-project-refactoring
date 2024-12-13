package com.gathering.review.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.review.model.entitiy.BookReview;
import com.gathering.review.model.entitiy.GatheringReview;
import lombok.*;

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
}
