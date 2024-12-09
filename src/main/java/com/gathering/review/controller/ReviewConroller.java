package com.gathering.review.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.review.model.dto.CreateReviewCommentDto;
import com.gathering.review.model.dto.CreateReviewDto;
import com.gathering.review.model.dto.ReviewDto;
import com.gathering.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
@Tag(name = "리뷰 API", description = "리뷰 관련 api")
public class ReviewConroller {

    private final ReviewService reviewService;


    /*
    * TODO
    *  리뷰가 특정 모임 참석 이후 책을 다읽엇을떄 작성이 가능한지
    *  특정 책에 대한 리뷰 작성이 아무때나 가능한지 확인 필요
    */
    @PostMapping("/create")
    @Operation(summary = "리뷰 생성", description = "appr -> 평가.<br>" +
            "tmprStrgYN -> 임시저장여부.<br>")
    public BaseResponse<ReviewDto> createReview(@RequestBody @Valid CreateReviewDto createReviewDto,
                                     @AuthenticationPrincipal UserDetails userDetails) {

        ReviewDto reviewDto = reviewService.createReview(createReviewDto, userDetails.getUsername());

        // TODO 추후 예외처리 상세화 필요
        if(reviewDto == null) {
            return new BaseResponse<>(BaseResponseStatus.UNKNOWN_ERROR);
        }
        return new BaseResponse<>(reviewDto);

    }

    @PostMapping("/comment/create")
    @Operation(summary = "댓글 생성", description = "대댓글의 경우 parent값을 부모 댓글의 id값을 넣으시면 됩니다.<br>" +
            "댓글일 경우 parent값을 0으로 넣으시면 됩니다.<br>")
    public BaseResponse<ReviewDto> createReviewComment(@RequestBody @Valid CreateReviewCommentDto createReviewCommentDto,
                                                @AuthenticationPrincipal UserDetails userDetails) {

        ReviewDto reviewDto = reviewService.createReviewComment(createReviewCommentDto, userDetails.getUsername());

        return new BaseResponse<>(reviewDto);

    }


}
