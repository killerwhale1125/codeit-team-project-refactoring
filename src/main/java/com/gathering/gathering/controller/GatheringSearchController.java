package com.gathering.gathering.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering.model.dto.GatheringResponse;
import com.gathering.gathering.model.dto.GatheringSearch;
import com.gathering.gathering.model.dto.GatheringSearchResponse;
import com.gathering.gathering.service.search.GatheringSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gatheringSearch")
@Tag(name = "모임 관련 검색 API", description = "모임 관련 검색 api")
@ApiResponse(responseCode = "200", description = "success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
public class GatheringSearchController {

    private final GatheringSearchService gatheringSearchService;

    /**
     * 모임 상태에 따른 모임 리스트 조회
     * - 날짜 ( 시작일, 종료일 ) -> 유효성 검증 필요
     * - 목표 독서 시간 ( 1 ~ 12 시간 )
     * - 정렬 기준 -> 마감 임박, 참여 인원, 조회수, 신규 모임 순
     *
     * TODO - 필터가 아예 설정되지 않을 경우 Default값은?
     *      - 쿼리 최적화 필요
     */
    @GetMapping
    @Operation(summary = "모임 필터링 검색", description = "모임 필터링 검색 (와이어 프레임 확인 후 데이터 추가 필요)")
    public BaseResponse<GatheringSearchResponse> findGatherings(@ModelAttribute GatheringSearch gatheringSearch,
                                                                @PageableDefault(page = 0, size = 10, sort = "id,desc") Pageable pageable) {
        return new BaseResponse<>(gatheringSearchService
                .findGatherings(gatheringSearch, pageable));
    }

    /**
     * TODO - Challenge 정보 추가 여부 보류
     */
    @GetMapping("/{gatheringId}")
    @Operation(summary = "모임 상세 조회", description = "모임 상세 조회 API, Challenge 정보 추가 여부 보류 (와이어 프레임 확인 후 데이터 추가 필요)")
    public BaseResponse<GatheringResponse> getGatheringByGatheringId(@PathVariable Long gatheringId) {
        return new BaseResponse<>(gatheringSearchService.getById(gatheringId));
    }
}
