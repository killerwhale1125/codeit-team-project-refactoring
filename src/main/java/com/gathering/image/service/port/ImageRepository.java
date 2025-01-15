package com.gathering.image.service.port;

import com.gathering.image.domain.ImageDomain;
import com.gathering.image.infrastructure.entity.Image;

import java.util.List;

public interface ImageRepository {
    Image findById(Long imageId);

    void delete(Image image);

    List<Image> findImageByGatheringId(Long gatheringId);

    ImageDomain save(ImageDomain image);

    List<ImageDomain> saveAll(List<ImageDomain> images);
}
