package com.gathering.gathering.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.domain.GatheringCreate;
import com.gathering.gathering.controller.response.MyPageGatheringsCountResponse;
import com.gathering.gathering_user.domain.GatheringUserStatus;
import com.gathering.gathering.domain.GatheringWeek;
import com.gathering.gathering.controller.port.GatheringService;
import com.gathering.user.controller.response.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/gathering")
@RequiredArgsConstructor
public class GatheringController {

    private final GatheringService gatheringService;

    @PostMapping
    public BaseResponse<GatheringDomain> create(@RequestPart("gatheringCreate") @Valid GatheringCreate gatheringCreate,
                                                @RequestPart(value = "file", required = false) List<MultipartFile> files,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        return new BaseResponse<>(gatheringService.create(gatheringCreate, files, userDetails.getUsername()));
    }

    /**
     * TODO - 참여 클릭 시 동시성 제어 추후 필요
     */
    @PostMapping("/{gatheringId}/join")
    public BaseResponse<Void> join(@PathVariable Long gatheringId,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.join(gatheringId, userDetails.getUsername());
        return new BaseResponse<>();
    }

    @PostMapping("/{gatheringId}/leave")
    public BaseResponse<Void> leave(@PathVariable Long gatheringId, @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.leave(gatheringId, userDetails.getUsername());
        return new BaseResponse<>();
    }

    @DeleteMapping("/{gatheringId}")
    public BaseResponse<Void> delete(@PathVariable Long gatheringId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.delete(gatheringId, userDetails.getUsername());
        return new BaseResponse<>();
    }

    @GetMapping("/{gatheringId}/users")
    public BaseResponse<List<UserResponseDto>> findByGatheringIdAndStatusWithUsers(@PathVariable Long gatheringId,
                                                                          @RequestParam GatheringUserStatus gatheringUserStatus) {
        return new BaseResponse<>(gatheringService.findByGatheringIdAndStatusWithUsers(gatheringId, gatheringUserStatus));
    }

    @PostMapping("/{gatheringId}/wish")
    public BaseResponse<Void> wish(@PathVariable Long gatheringId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.wish(gatheringId, userDetails.getUsername());
        return new BaseResponse<>();
    }

    @GetMapping("/calculate-end-date")
    public BaseResponse<LocalDate> endDate(@RequestParam LocalDate startDate, GatheringWeek gatheringWeek) {
        return new BaseResponse<>(gatheringService.calculateEndDate(startDate, gatheringWeek));
    }

    @GetMapping("/mypage-count")
    public BaseResponse<MyPageGatheringsCountResponse> getMyPageGatheringsCount(@AuthenticationPrincipal UserDetails userDetails) {
        return new BaseResponse<>(gatheringService.getMyPageGatheringsCount(userDetails.getUsername()));
    }

    @PostMapping("/{gatheringId}/read-book")
    public BaseResponse<Void> readBookCompleted(
            @PathVariable Long gatheringId,
            @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.readBookCompleted(userDetails.getUsername(), gatheringId);
        return new BaseResponse<>();
    }
}
