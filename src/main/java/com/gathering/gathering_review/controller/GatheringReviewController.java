package com.gathering.gathering_review.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering_review.controller.port.GatheringReviewService;
import com.gathering.gathering_review.controller.response.GatheringReviewResponse;
import com.gathering.gathering_review.domain.GatheringReviewCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gathering/{gatheringId}/reviews")
public class GatheringReviewController {

    private final GatheringReviewService gatheringReviewService;

    @PostMapping
    public BaseResponse<GatheringReviewResponse> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody GatheringReviewCreate gatheringReviewCreate) {
        return new BaseResponse<>(gatheringReviewService.create(userDetails.getUsername(), gatheringReviewCreate));
    }
}
