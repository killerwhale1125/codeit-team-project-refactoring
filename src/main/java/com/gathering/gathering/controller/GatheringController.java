package com.gathering.gathering.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.service.GatheringService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gathering")
@RequiredArgsConstructor
public class GatheringController {

    private final GatheringService gatheringService;

    /**
     * 모임 생성
     */
    @PostMapping
    public BaseResponse<Void> create(@RequestBody GatheringCreate gatheringCreate, @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.create(gatheringCreate, userDetails);
        return new BaseResponse<>();
    }
}
