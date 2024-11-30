package com.gathering.gathering.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.service.GatheringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "모임 API", description = "모임 관련 api")
@ApiResponse(responseCode = "200", description = "success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
public class GatheringController {

    private final GatheringService gatheringService;

    /**
     * 모임 생성 API
     * @param gatheringCreate
     * @param userDetails
     * @return
     */
    @PostMapping
    @Operation(summary = "모임 생성", description = "모임 생성 API")
    public BaseResponse<Void> create(@RequestBody @Valid GatheringCreate gatheringCreate,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        gatheringService.create(gatheringCreate, userDetails);
        return new BaseResponse<>();
    }
}
