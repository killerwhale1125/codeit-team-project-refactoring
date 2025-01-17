package com.gathering.gathering.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering.controller.port.GatheringSearchService;
import com.gathering.gathering.controller.response.GatheringResponse;
import com.gathering.gathering.controller.response.GatheringSearchResponse;
import com.gathering.gathering.domain.GatheringSearch;
import com.gathering.gathering.domain.GatheringStatus;
import com.gathering.gathering.domain.SearchType;
import com.gathering.gathering_user.domain.GatheringUserStatus;
import com.gathering.util.web.UserSessionKeyGenerator;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

    /* 모임 필터링 검색 */
    @GetMapping("/filtering")
    public BaseResponse<GatheringSearchResponse> findGatheringsByFilters(@ModelAttribute GatheringSearch gatheringSearch,
                                                                @RequestParam("page") int page,
                                                                @RequestParam("size") int size,
                                                                @AuthenticationPrincipal UserDetails userDetails) {
        return new BaseResponse<>(gatheringSearchService.findGatheringsByFilters(gatheringSearch, page, size, getUsernameOrElseGet(userDetails)));
    }

    /* 참여 가능한 모임 조회 */
    @GetMapping("/joinable")
    public BaseResponse<GatheringSearchResponse> findJoinableGatherings(@ModelAttribute GatheringSearch gatheringSearch,
                                                                        @RequestParam("page") int page,
                                                                        @RequestParam("size") int size,
                                                                @AuthenticationPrincipal UserDetails userDetails) {
        return new BaseResponse<>(gatheringSearchService.findJoinableGatherings(gatheringSearch, page, size, getUsernameOrElseGet(userDetails)));
    }

    /* 내가 참여중인 모임 리스트 */
    @GetMapping("/participating")
    public BaseResponse<GatheringSearchResponse> findMyGatherings(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestParam(required = false) GatheringUserStatus gatheringUserStatus,
                                                                  @RequestParam(required = false) GatheringStatus gatheringStatus,
                                                                  @RequestParam("page") int page,
                                                                  @RequestParam("size") int size) {
        return new BaseResponse<>(gatheringSearchService.findMyGatherings(userDetails.getUsername(), page, size, gatheringStatus, gatheringUserStatus));
    }

    /* 모임 상세 조회 */
    @GetMapping("/{gatheringId}/detail")
    public BaseResponse<GatheringResponse> getById(@PathVariable Long gatheringId,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        String userKey = UserSessionKeyGenerator.generateUserKey(request, response);
        return new BaseResponse<>(gatheringSearchService.getById(gatheringId, userKey, getUsernameOrElseGet(userDetails)));
    }

    /* 내가 만든 모임 리스트 */
    @GetMapping("/my")
    public BaseResponse<GatheringSearchResponse> my(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        return new BaseResponse<>(gatheringSearchService.findMyCreated(userDetails.getUsername(), page, size));
    }

    /* 내가 찜한 모임 리스트 */
    @GetMapping("/wishes")
    public BaseResponse<GatheringSearchResponse> wishes(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestParam("page") int page,
                                                        @RequestParam("size") int size) {
        return new BaseResponse<>(gatheringSearchService.findMyWishes(userDetails.getUsername(), page, size));
    }

    /* 모임 소개 */
    @GetMapping("/{gatheringId}/introduce")
    public BaseResponse<GatheringResponse> introduce(@PathVariable Long gatheringId) {
        return new BaseResponse<>(gatheringSearchService.introduce(gatheringId));
    }

    /* 검색어로 검색 후 모임탭에서 모임 리스트 결과 페이징 조회 */
    @GetMapping("/search-gatherings")
    public BaseResponse<GatheringSearchResponse> gatheringSearch(@RequestParam String searchWord,
                                                                 @RequestParam SearchType searchType,
                                                                 @RequestParam("page") int page,
                                                                 @RequestParam("size") int size) {
        return new BaseResponse<>(gatheringSearchService.getGatheringsBySearchWordAndType(searchWord, searchType, page, size));
    }

    private static String getUsernameOrElseGet(UserDetails userDetails) {
        return userDetails != null ? userDetails.getUsername() : null;
    }
}
