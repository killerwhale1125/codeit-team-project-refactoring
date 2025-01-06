package com.gathering.review.controller;

import com.gathering.book.model.dto.BookResponse;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering.model.entity.SearchType;
import com.gathering.review.model.constant.BookReviewTagType;
import com.gathering.review.model.constant.ReviewType;
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

import java.util.List;

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
            @PathVariable ReviewType type,
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
    public BaseResponse<ReviewCommentDto> createReviewComment(
            @RequestBody @Valid CreateReviewCommentDto createReviewCommentDto,
            @AuthenticationPrincipal UserDetails userDetails) {


        ReviewCommentDto commentDto = reviewService.createReviewComment(createReviewCommentDto, userDetails.getUsername());

        return new BaseResponse<>(commentDto);

    }
    @GetMapping("/user/{type}")
    @Operation(summary = "나의 리뷰", description = "상세 조건 Notion 참고")
    public BaseResponse<ReviewListDto> selectUserReviewList(
            @PathVariable ReviewType type,
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(page = 0, size = 5, sort = "id,desc") Pageable pageable) {


        ReviewListDto result = reviewService.selectUserReviewList(userDetails.getUsername(), type, pageable);

        return new BaseResponse<>(result);

    }


    @GetMapping
    @Operation(summary = "독서 리뷰", description = "상세 조건 Notion 참고")
    public BaseResponse<ReviewListDto> selectBookReviewList(
            @AuthenticationPrincipal UserDetails userDetails) {
        String name = null;
        if(userDetails != null) {
            name = userDetails.getUsername();
        }
        ReviewListDto result = reviewService.selectBookReviewList(name);
        return new BaseResponse<>(result);

    }

    @GetMapping("/search/tag")
    @Operation(summary = "리뷰 필터링 검색 ( 무한 스크롤 전용 )", description = "상세 조건 Notion 참고")
    public BaseResponse<ReviewListDto> findReviews(
            @RequestParam BookReviewTagType tag,
            @PageableDefault(page = 0, size = 10, sort = "id,desc") Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = null;
        if(userDetails != null) {
            username = userDetails.getUsername();
        }
        return new BaseResponse<>(reviewService.findReviews(tag, pageable, username));
    }

    @GetMapping("/{reviewId}/detail")
    @Operation(summary = "독서 리뷰 상세 조회", description = "상세 조건 Notion 참고")
    public BaseResponse<ReviewDto> selectBookReviewDetail(
            @PathVariable long reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = null;
        if(userDetails != null) {
            username = userDetails.getUsername();
        }
        return new BaseResponse<>(reviewService.selectBookReviewDetail(reviewId, username));
    }

    @GetMapping("/search")
    @Operation(summary = "리뷰 검색", description = "상세 조건 Notion 참고")
    public BaseResponse<ReviewListDto> searchReviews(
            @RequestParam @Valid SearchType type,
            @RequestParam String searchParam,
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(page = 0, size = 5, sort = "id,desc") Pageable pageable) {

        String username = null;
        if(userDetails != null) {
            username = userDetails.getUsername();
        }

        return new BaseResponse<>(reviewService.searchReviews(type, searchParam, pageable, username));
    }


    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "상세 조건 Notion 참고")
    public BaseResponse<Void> DeleteReview(
            @PathVariable long reviewId,
            @RequestParam ReviewType type,
            @AuthenticationPrincipal UserDetails userDetails) {
        int result = reviewService.DeleteReview(reviewId, type, userDetails.getUsername());

        if(result != 1) {
            throw new BaseException(BaseResponseStatus.REVIEW_DELETED_FAILED);
        }
        return new BaseResponse<>();
    }

    @PutMapping("/{type}/{reviewId}/edit")
    @Operation(summary = "리뷰 수정", description = "상세 조건 Notion 참고")
    public BaseResponse<Void> UpdateReview(
            @RequestBody @Valid CreateReviewDto editReviewDto,
            @PathVariable ReviewType type,
            @PathVariable long reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {

        reviewService.UpdateReview(editReviewDto, reviewId, type, userDetails.getUsername());
        return new BaseResponse<>();
    }

    @PostMapping("/like")
    @Operation(summary = "리뷰 좋아요", description = "상세 조건 Notion 참고")
    public BaseResponse<Void> UpdateReviewLike(
            @RequestBody ReviewLikeDto reviewLikeDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        reviewService.UpdateReviewLike(reviewLikeDto, userDetails.getUsername());
        return new BaseResponse<>();
    }

    
    @DeleteMapping("/{commentId}/comment")
    @Operation(summary = "댓글 삭제", description = "상세 조건 Notion 참고")
    public BaseResponse<Void> DeleteComment(
            @PathVariable long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {

        int result = reviewService.DeleteComment(commentId, userDetails.getUsername());

        if(result != 1) {
            throw new BaseException(BaseResponseStatus.COMMENT_DELETED_FAILED);
        }

        return new BaseResponse<>();
    }
    @PutMapping("/{commentId}/comment/edit")
    @Operation(summary = "댓글 수정", description = "상세 조건 Notion 참고")
    public BaseResponse<Void> UpdateComment(
            @PathVariable long commentId,
            @RequestBody @Valid CreateReviewCommentDto updateReviewCommentDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        int result = reviewService.UpdateComment(commentId,updateReviewCommentDto, userDetails.getUsername());

        if(result != 1) {
            throw new BaseException(BaseResponseStatus.COMMENT_UPDATE_FAILED);
        }

        return new BaseResponse<>();
    }

    @GetMapping("/user/gathering")
    @Operation(summary = "내가 참여한 모임의 책 목록", description = "Notion 참고")
    public BaseResponse<List<BookResponse>> searchUserGatheringBooks(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return new BaseResponse<>(reviewService.searchUserGatheringBooks(userDetails.getUsername()));
    }


    @GetMapping("/recommendedKeywords")
    @Operation(summary = "추천 검색어", description = "Notion 참고")
    public BaseResponse<List<BookResponse>> getRecommendedKeywords() {
        return new BaseResponse<>(reviewService.getRecommendedKeywords());
    }

}
