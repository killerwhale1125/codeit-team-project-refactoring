package com.gathering.image.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.image.service.GatheringImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gathering")
public class GatheringImageController {
    private final GatheringImageUploadService gatheringImageUploadService;

    @PostMapping("/{gatheringId}/images")
    public BaseResponse<Void> uploadImages(@PathVariable Long gatheringId,
                                           @RequestParam("file") List<MultipartFile> files) throws IOException {
        gatheringImageUploadService.upload(gatheringId, files);

        return new BaseResponse<>();
    }
}
