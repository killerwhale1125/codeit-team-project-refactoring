package com.gathering.review.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.book.model.dto.BookResponse;
import com.gathering.gathering.controller.response.GatheringResponse;
import com.querydsl.core.Tuple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

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
    private boolean hasNext;

    //나의 리뷰 -> 모임 후기에 필요한 데이터
    private long writableReviewCount;
    private long writedReviewCount;


    // 모임 후기의 비율을 나타내는 데이터
    private int good;
    private int soso;
    private int bad;

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
    public static ReviewListDto fromBookReviews(List<BookReviewDto> bookReviews, boolean hasNext) {
        return ReviewListDto.builder()
                .bookReviews(bookReviews)
                .hasNext(hasNext)
                .build();
    }

    // 리뷰 상세의 특정 책 리뷰 목록 및 통합 검색 리뷰 결과 목록
    public static ReviewListDto fromBookReviews(List<BookReviewDto> bookReviews, long total) {
        return ReviewListDto.builder()
                .bookReviews(bookReviews)
                .total(total)
                .build();
    }


    public static ReviewListDto fromGatheringReviews(List<BookResponse> bookResponseList,List<BookReviewDto> bookReviews, long total) {
        return ReviewListDto.builder()
                .bookReviews(bookReviews)
                .bookResponseList(bookResponseList)
                .total(total)
                .build();
    }
    // 나의 리뷰 -> 모임 후기
    
    public static ReviewListDto fromGatheringReviews(List<GatheringResponse> gatheringResponses, long writableReviewCount
                                                     ,List<GatheringReviewDto> gatheringReviews, long writedReviewCount) {
        return ReviewListDto.builder()
                .gatheringResponses(gatheringResponses)
                .writableReviewCount(writableReviewCount)
                .gatheringReviews(gatheringReviews)
                .writedReviewCount(writedReviewCount)
                .build();
    }


    public static ReviewListDto fromGatheringReviews(List<GatheringReviewDto> gatheringReviews, Tuple result, boolean hasNext) {
        return ReviewListDto.builder()
                .gatheringReviews(gatheringReviews)
                .total(result.get(0, Long.class))
                .scoreAvg(Optional.ofNullable(result.get(1, Double.class)).orElse(0.0))
                .good((int) ((double) result.get(2, Long.class) / result.get(0, Long.class) * 100))
                .soso((int) ((double) result.get(3, Long.class) / result.get(0, Long.class) * 100))
                .bad((int) ((double) result.get(4, Long.class) / result.get(0, Long.class) * 100))
                .hasNext(hasNext)
                .build();
    }
}
