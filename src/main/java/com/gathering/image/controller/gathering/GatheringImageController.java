package com.gathering.image.controller.gathering;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.image.service.gathering.GatheringImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gathering")
@Tag(name = "모임 이미지 추가 및 삭제 API", description = "모임 이미지 추가 및 삭제 API")
@ApiResponse(responseCode = "200", description = "success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
public class GatheringImageController {
    private final GatheringImageService gatheringImageService;

    @PostMapping("/{gatheringId}/images")
    @Operation(summary = "모임 이미지 업로드", description = "상세 조건 Notion 참고")
    public BaseResponse<Void> uploadImages(@RequestParam("file") List<MultipartFile> files) throws IOException {
        gatheringImageService.uploadGatheringImage(files);

        return new BaseResponse<>();
    }

    @DeleteMapping("/{gatheringId}/images")
    @Operation(summary = "모임 이미지 삭제", description = "상세 조건 Notion 참고")
    public BaseResponse<Void> delete(@PathVariable Long gatheringId) {
        gatheringImageService.delete(gatheringId);
        return new BaseResponse<>();
    }

}
