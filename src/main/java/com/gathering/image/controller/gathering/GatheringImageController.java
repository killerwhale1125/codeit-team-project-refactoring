package com.gathering.image.controller.gathering;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.image.model.entity.EntityType;
import com.gathering.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gathering")
public class GatheringImageController {
    private final ImageService imageService;

//    @PostMapping("/{gatheringId}/images")
//    public BaseResponse<Void> uploadImages(@RequestParam("file") List<MultipartFile> files, @RequestParam EntityType entityType) throws IOException {
//        imageService.uploadImage(files, entityType);
//
//        return new BaseResponse<>();
//    }

//    @DeleteMapping("/{gatheringId}/images")
//    @Operation(summary = "모임 이미지 삭제", description = "상세 조건 Notion 참고")
//    public BaseResponse<Void> delete(@PathVariable Long gatheringId) {
//        gatheringImageService.delete(gatheringId);
//        return new BaseResponse<>();
//    }

}
