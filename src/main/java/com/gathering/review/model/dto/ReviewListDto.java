package com.gathering.review.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.book.model.dto.BookResponse;
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

    // 작성하지 않은 리뷰 책정보
    private List<BookResponse> bookResponseList;

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


    // 리뷰 조회 무한 스크롤
    public static ReviewListDto fromGatheringReviews(List<BookReviewDto> bookReviews, boolean hasNext) {
        return ReviewListDto.builder()
                .bookReviews(bookReviews)
                .hasNext(hasNext)
                .build();
    }


    public static ReviewListDto fromGatheringReviews(List<BookResponse> bookResponseList,List<BookReviewDto> bookReviews, long total) {
        return ReviewListDto.builder()
                .bookReviews(bookReviews)
                .bookResponseList(bookResponseList)
                .total(total)
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
