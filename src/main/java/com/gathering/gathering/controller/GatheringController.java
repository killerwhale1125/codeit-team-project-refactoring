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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "모임 API", description = "모임 관련 api")
@ApiResponse(responseCode = "200", description = "success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
public class GatheringController {

    private final GatheringService gatheringService;

    /**
     * TODO -
     */
    @PostMapping
    @Operation(summary = "모임 생성", description = "gatheringStatus는 RECRUITING, 최소 인원 5 이상 최대인원 6 이상")
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
    @Operation(summary = "모임 참여", description = "Notion 참고")
    public BaseResponse<Void> join(@PathVariable Long gatheringId,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.join(gatheringId, userDetails.getUsername());
        return new BaseResponse<>();
    }

    @PostMapping("/{gatheringId}/leave")
    @Operation(summary = "모임 떠나기", description = "Notion 참고")
    public BaseResponse<Void> leave(@PathVariable Long gatheringId,
                                    @RequestBody GatheringRequest gatheringRequest,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.leave(gatheringId, userDetails.getUsername(), gatheringRequest.getGatheringUserStatus());
        return new BaseResponse<>();
    }

    @DeleteMapping("/{gatheringId}")
    @Operation(summary = "모임 삭제", description = "Notion 참고")
    public BaseResponse<Void> delete(@PathVariable Long gatheringId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.delete(gatheringId, userDetails.getUsername());
        return new BaseResponse<>();
    }

    /**
     * TODO -
     */
    @GetMapping("/{gatheringId}/users")
    @Operation(summary = "모임 참여 상태에 따른 유저 조회", description = "참여 상태 -> PARTICIPATING(참여 O), NOT_PARTICIPATING(참여 X)")
    public BaseResponse<List<UserResponseDto>> findGatheringWithUsersByIdAndStatus(@PathVariable Long gatheringId,
                                                                                   @RequestParam GatheringUserStatus gatheringUserStatus) {
        return new BaseResponse<>(gatheringService.findGatheringWithUsersByIdAndStatus(gatheringId, gatheringUserStatus));
    }

    /**
     * TODO -
     */
    @PostMapping("/{gatheringId}/wish")
    @Operation(summary = "모임 찜하기", description = "Notion 참고")
    public BaseResponse<Void> wish(@PathVariable Long gatheringId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.wish(gatheringId, userDetails.getUsername());
        return new BaseResponse<>();
    }

    @GetMapping("/calculate-end-date")
    @Operation(summary = "모임 생성 전 종료일 계산 API", description = "Notion 참고")
    public BaseResponse<LocalDate> endDate(@RequestParam LocalDate startDate, GatheringWeek gatheringWeek) {
        return new BaseResponse<>(gatheringService.calculateEndDate(startDate, gatheringWeek));
    }

    @GetMapping("/mypage-count")
    @Operation(summary = "마이페이지 총 모임 갯수", description = "Notion 참고")
    public BaseResponse<MyPageGatheringsCountResponse> getMyPageGatheringsCount(@AuthenticationPrincipal UserDetails userDetails) {
        return new BaseResponse<>(gatheringService.getMyPageGatheringsCount(userDetails.getUsername()));
    }

    @PostMapping("/{gatheringId}/read-book")
    @Operation(summary = "모임 독서 기록", description = "Notion 참고")
    public BaseResponse<Void> readBook(
            @PathVariable long gatheringId,
            @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.readBook(userDetails.getUsername(), gatheringId);
        return new BaseResponse<>();
    }
}
