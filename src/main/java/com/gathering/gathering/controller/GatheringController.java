package com.gathering.gathering.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering.model.domain.GatheringDomain;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.dto.GatheringRequest;
import com.gathering.gathering.model.dto.GatheringUpdate;
import com.gathering.gathering.model.dto.MyPageGatheringsCountResponse;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.gathering.service.GatheringService;
import com.gathering.user.model.dto.response.UserResponseDto;
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

    @PatchMapping("/{gatheringId}")
    public BaseResponse<Void> update(@RequestBody @Valid GatheringUpdate gatheringUpdate, @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.update(gatheringUpdate, userDetails.getUsername());
        return null;
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
    public BaseResponse<Void> leave(@PathVariable Long gatheringId,
                                    @RequestBody GatheringRequest gatheringRequest,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.leave(gatheringId, userDetails.getUsername(), gatheringRequest.getGatheringUserStatus());
        return new BaseResponse<>();
    }

    @DeleteMapping("/{gatheringId}")
    public BaseResponse<Void> delete(@PathVariable Long gatheringId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.delete(gatheringId, userDetails.getUsername());
        return new BaseResponse<>();
    }

    @GetMapping("/{gatheringId}/users")
    public BaseResponse<List<UserResponseDto>> findGatheringWithUsersByIdAndStatus(@PathVariable Long gatheringId,
                                                                                   @RequestParam GatheringUserStatus gatheringUserStatus) {
        return new BaseResponse<>(gatheringService.findGatheringWithUsersByIdAndStatus(gatheringId, gatheringUserStatus));
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
    public BaseResponse<Void> readBook(
            @PathVariable long gatheringId,
            @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.readBook(userDetails.getUsername(), gatheringId);
        return new BaseResponse<>();
    }
}
