package com.gathering.gathering.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.model.entity.GatheringReviewSortType;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import com.gathering.gathering.model.entity.SearchType;
import com.gathering.gathering.service.search.GatheringSearchService;
import com.gathering.review.model.dto.ReviewListDto;
import com.gathering.util.web.UserSessionKeyGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    @GetMapping("/filtering")
    @Operation(summary = "모임 필터링 검색 ( 무한 스크롤 전용 )", description = "상세 조건 Notion 참고")
    public BaseResponse<GatheringSearchResponse> findGatherings(@ModelAttribute GatheringSearch gatheringSearch,
                                                                @PageableDefault(page = 0, size = 10, sort = "id,desc") Pageable pageable,
                                                                @AuthenticationPrincipal UserDetails userDetails) {
        return new BaseResponse<>(gatheringSearchService.findGatherings(gatheringSearch, pageable, userDetails));
    }

    @GetMapping("/joinable")
    @Operation(summary = "참여 가능한 모임 조회", description = "상세 조건 Notion 참고")
    public BaseResponse<GatheringSearchResponse> findJoinableGatherings(@ModelAttribute GatheringSearch gatheringSearch,
                                                                @PageableDefault(page = 0, size = 10, sort = "id,desc") Pageable pageable,
                                                                @AuthenticationPrincipal UserDetails userDetails) {
        return new BaseResponse<>(gatheringSearchService.findJoinableGatherings(gatheringSearch, pageable, userDetails));
    }

    @GetMapping("/participating")
    @Operation(summary = "내가 참여중인 모임 리스트", description = "상세 조건 Notion 참고")
    public BaseResponse<GatheringSearchResponse> findMyGatherings(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestParam(required = false) GatheringUserStatus gatheringUserStatus,
                                                                  @RequestParam(required = false) GatheringStatus gatheringStatus,
                                                                  @PageableDefault(page = 0, size = 10, sort = "id,desc") Pageable pageable) {
        return new BaseResponse<>(gatheringSearchService.findMyGatherings(userDetails.getUsername(), pageable, gatheringStatus, gatheringUserStatus));
    }

    /**
     * TODO - Challenge 정보 추가 여부 보류
     */
    @GetMapping("/{gatheringId}/detail")
    @Operation(summary = "모임 상세 조회", description = "상세 조건 Notion 참고")
    public BaseResponse<GatheringResponse> getById(@PathVariable Long gatheringId,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        String userKey = UserSessionKeyGenerator.generateUserKey(request, response);
        return new BaseResponse<>(gatheringSearchService.getById(gatheringId, userKey, userDetails));
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

    @Operation(summary = "모임 리뷰", description = "상세 조건 Notion 참고")
    @GetMapping("/{gatheringId}/review")
    public BaseResponse<ReviewListDto> review(@PathVariable Long gatheringId,
                                              @RequestParam @Valid GatheringReviewSortType sort,
                                              @PageableDefault(page = 0, size = 5, sort = "id,desc") Pageable pageable) {
        return new BaseResponse<>(gatheringSearchService.review(gatheringId, sort, pageable));
    }

    /**
     * 처음 검색은 integrated 호출
     */
    @GetMapping("/search-integrated")
    @Operation(summary = "검색 기능 (검색어로 처음 검색했을 때)", description = "상세 조건 Notion 참고")
    public BaseResponse<GatheringSearchResponse> search(@RequestParam String searchWord,
                                                        @RequestParam SearchType searchType,
                                                        @AuthenticationPrincipal UserDetails userDetails,
                                                        @PageableDefault(page = 0, size = 5) Pageable pageable) {

        String username = null;
        if(userDetails != null) {
            username = userDetails.getUsername();
        }

        return new BaseResponse<>(gatheringSearchService.getIntegratedResultBySearchWordAndType(searchWord, searchType, pageable, username));
    }

    /**
     * 검색된 이후 부터는 각각 호출
     */
    @GetMapping("/search-gatherings")
    @Operation(summary = "검색어로 검색 후 모임탭에서 페이징 조회", description = "상세 조건 Notion 참고")
    public BaseResponse<GatheringSearchResponse> gatheringSearch(@RequestParam String searchWord,
                                                                 @RequestParam SearchType searchType,
                                                                 @PageableDefault(page = 0, size = 5) Pageable pageable) {
        return new BaseResponse<>(gatheringSearchService.getGatheringsBySearchWordAndType(searchWord, searchType, pageable));
    }

    /**
     * 검색된 이후 부터는 각각 호출
     */
    @GetMapping("/search-reviews")
    @Operation(summary = "검색어로 검색 후 리뷰탭에서 페이징 조회", description = "상세 조건 Notion 참고")
    public BaseResponse<GatheringSearchResponse> reviewSearch(@RequestParam String searchWord,
                                                                 @RequestParam SearchType searchType,
                                                                 @AuthenticationPrincipal UserDetails userDetails,
                                                                 @PageableDefault(page = 0, size = 5) Pageable pageable) {
        String username = null;
        if(userDetails != null) {
            username = userDetails.getUsername();
        }

        return new BaseResponse<>(gatheringSearchService.getReviewsBySearchWordAndType(searchWord, searchType, pageable, username));
    }
}
