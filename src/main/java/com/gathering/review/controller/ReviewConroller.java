package com.gathering.review.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering.model.entity.GatheringReviewSortType;
import com.gathering.review.model.constant.BookReviewTagType;
import com.gathering.review.model.dto.*;
import com.gathering.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@Tag(name = "리뷰 API", description = "리뷰 관련 api")
public class ReviewConroller {

    private final ReviewService reviewService;


    @PostMapping("/create/{type}")
    @Operation(summary = "리뷰 생성", description = "상세 조건 Notion 참고")
    public BaseResponse<ReviewDto> createReview(
            @RequestBody @Valid CreateReviewDto createReviewDto,
            @PathVariable String type,
            @AuthenticationPrincipal UserDetails userDetails) {

        ReviewDto reviewDto = reviewService.createReview(createReviewDto, userDetails.getUsername(), type);

        // TODO 추후 예외처리 상세화 필요
        if(reviewDto == null) {
            return new BaseResponse<>(BaseResponseStatus.UNKNOWN_ERROR);
        }
        return new BaseResponse<>(reviewDto);

    }

    @PostMapping("/comment/create")
    @Operation(summary = "댓글 생성", description = "상세 조건 Notion 참고")
    public BaseResponse<ReviewCommentDto> createReviewComment(@RequestBody @Valid CreateReviewCommentDto createReviewCommentDto,
                                                @AuthenticationPrincipal UserDetails userDetails) {

        ReviewCommentDto commentDto = reviewService.createReviewComment(createReviewCommentDto, userDetails.getUsername());

        return new BaseResponse<>(commentDto);

    }
    @GetMapping("/user/{type}")
    @Operation(summary = "나의 리뷰", description = "상세 조건 Notion 참고")
    public BaseResponse<ReviewListDto> selectUserReviewList(
            @PathVariable String type,
            @AuthenticationPrincipal UserDetails userDetails) {

        ReviewListDto result = reviewService.selectUserReviewList(userDetails.getUsername(), type);

        return new BaseResponse<>(result);

    }


    @GetMapping
    @Operation(summary = "독서 리뷰", description = "상세 조건 Notion 참고")
    public BaseResponse<ReviewListDto> selectBookReviewList(
            @AuthenticationPrincipal UserDetails userDetails) {

        ReviewListDto result = reviewService.selectBookReviewList(userDetails.getUsername());
        return new BaseResponse<>(null);

    }
}
