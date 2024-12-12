package com.gathering.gathering.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.service.search.GatheringSearchService;
import com.gathering.util.web.UserSessionKeyGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gatheringSearch")
@Tag(name = "모임 관련 검색 API", description = "모임 관련 검색 api")
@ApiResponse(responseCode = "200", description = "success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
public class GatheringSearchController {

    private final GatheringSearchService gatheringSearchService;

    /**
     * TODO - 필터가 아예 설정되지 않을 경우 Default값은?
     *      - 쿼리 최적화 필요
     */
    @GetMapping
    @Operation(summary = "모임 필터링 검색 ( 무한 스크롤 전용 )", description = "상세 조건 Notion 참고")
    public BaseResponse<GatheringSearchResponse> findGatherings(@ModelAttribute GatheringSearch gatheringSearch,
                                                                @PageableDefault(page = 0, size = 10, sort = "id,desc") Pageable pageable) {
        return new BaseResponse<>(gatheringSearchService.findGatherings(gatheringSearch, pageable));
    }

    @GetMapping("/participating")
    @Operation(summary = "내가 참여한 모임 리스트", description = "상세 조건 Notion 참고")
    public BaseResponse<GatheringSearchResponse> findMyGatherings(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestParam(required = false) GatheringUserStatus gatheringUserStatus,
                                                                  @RequestParam(required = false) GatheringStatus gatheringStatus,
                                                                  @PageableDefault(page = 0, size = 10, sort = "id,desc") Pageable pageable) {
        return new BaseResponse<>(gatheringSearchService.findMyGatherings(userDetails.getUsername(), pageable, gatheringStatus, gatheringUserStatus));
    }

    /**
     * TODO - Challenge 정보 추가 여부 보류
     */
    @GetMapping("/{gatheringId}")
    @Operation(summary = "모임 상세 조회", description = "상세 조건 Notion 참고")
    public BaseResponse<GatheringResponse> getById(@PathVariable Long gatheringId, HttpServletRequest request, HttpServletResponse response) {
        return new BaseResponse<>(gatheringSearchService.getById(gatheringId,
                UserSessionKeyGenerator.generateUserKey(request, response)));
    }

    @GetMapping("/my")
    @Operation(summary = "내가 만든 모임 리스트", description = "상세 조건 Notion 참고")
    public BaseResponse<GatheringSearchResponse> my(@AuthenticationPrincipal UserDetails userDetails,
                                                    @PageableDefault(page = 0, size = 10, sort = "id,desc") Pageable pageable) {
        return new BaseResponse<>(gatheringSearchService.findMyCreated(userDetails.getUsername(), pageable));
    }

    @GetMapping("/wishes")
    @Operation(summary = "내가 찜한 모임 리스트", description = "상세 조건 Notion 참고")
    public BaseResponse<GatheringSearchResponse> wishes(@AuthenticationPrincipal UserDetails userDetails,
                                                        @PageableDefault(page = 0, size = 10, sort = "id,desc") Pageable pageable) {
        return new BaseResponse<>(gatheringSearchService.findMyWishes(userDetails.getUsername(), pageable));
    }

    @Operation(summary = "모임 소개", description = "상세 조건 Notion 참고")
    @GetMapping("/{gatheringId}/introduce")
    public BaseResponse<GatheringResponse> introduce(@PathVariable Long gatheringId) {
        return new BaseResponse<>(gatheringSearchService.introduce(gatheringId));
    }
}
