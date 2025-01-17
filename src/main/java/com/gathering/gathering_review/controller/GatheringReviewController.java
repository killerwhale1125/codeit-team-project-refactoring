package com.gathering.gathering_review.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering_review.controller.port.GatheringReviewService;
import com.gathering.gathering_review.controller.response.GatheringReviewResponse;
import com.gathering.gathering_review.domain.GatheringReviewCreate;
import com.gathering.gathering_review.domain.GatheringReviewUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gathering-review")
public class GatheringReviewController {

    private final GatheringReviewService gatheringReviewService;

    @PostMapping
    public BaseResponse<GatheringReviewResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody GatheringReviewCreate gatheringReviewCreate) {
        return new BaseResponse<>(gatheringReviewService.create(userDetails.getUsername(), gatheringReviewCreate));
    }

    @PutMapping
    public BaseResponse<Void> update(
            @RequestBody @Valid GatheringReviewUpdate gatheringReviewUpdate,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {
        gatheringReviewService.update(gatheringReviewUpdate, reviewId, userDetails.getUsername());
        return new BaseResponse<>();
    }

    @DeleteMapping("/{reviewId}")
    public BaseResponse<Void> delete(
            @PathVariable long reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {
        gatheringReviewService.delete(reviewId, userDetails.getUsername());
        return new BaseResponse<>();
    }
}
