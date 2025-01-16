package com.gathering.gathering_review.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering_review.controller.port.GatheringReviewSearchService;
import com.gathering.gathering_review.controller.response.GatheringReviewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gathering-review")
public class GatheringReviewSearchController {

    private final GatheringReviewSearchService gatheringReviewSearchService;

    @GetMapping("/my-reviews")
    public BaseResponse<GatheringReviewsResponse> myReviews(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int page, @RequestParam int size) {
        return new BaseResponse<>(gatheringReviewSearchService.myReviews(userDetails.getUsername(), page, size));
    }
}
