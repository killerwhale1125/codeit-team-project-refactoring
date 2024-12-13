package com.gathering.review.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.review.model.dto.*;
import com.gathering.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@Tag(name = "리뷰 API", description = "리뷰 관련 api")
public class ReviewConroller {

    private final ReviewService reviewService;


    @PostMapping("/create/{type}")
    @Operation(summary = "리뷰 생성", description = "apprCd -> 평가.<br>" +
            "tmprStrgYN -> 임시저장여부.<br>")
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
    @Operation(summary = "댓글 생성", description = "대댓글의 경우 parent값을 부모 댓글의 id값을 넣으시면 됩니다.<br>" +
            "댓글일 경우 parent값을 0으로 넣으시면 됩니다.<br>")
    public BaseResponse<ReviewCommentDto> createReviewComment(@RequestBody @Valid CreateReviewCommentDto createReviewCommentDto,
                                                @AuthenticationPrincipal UserDetails userDetails) {

        ReviewCommentDto commentDto = reviewService.createReviewComment(createReviewCommentDto, userDetails.getUsername());

        return new BaseResponse<>(commentDto);

    }
    @GetMapping("/user/{type}")
    @Operation(summary = "나의 리뷰", description = "나의 리뷰")
    public BaseResponse<ReviewListDto> selectUserReviewList(
            @PathVariable String type,
            @AuthenticationPrincipal UserDetails userDetails) {

        ReviewListDto result = reviewService.selectUserReviewList(userDetails.getUsername(), type);

        return new BaseResponse<>(result);

    }
}
