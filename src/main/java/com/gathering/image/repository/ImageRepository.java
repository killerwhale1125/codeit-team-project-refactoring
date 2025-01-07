package com.gathering.image.repository;

import com.gathering.image.model.domain.ImageDomain;
import com.gathering.image.model.entity.Image;

import java.util.List;

public interface ImageRepository {
    Image findById(Long imageId);

    void delete(Image image);

    List<Image> findImageByGatheringId(Long gatheringId);

    ImageDomain save(ImageDomain image);

    List<ImageDomain> saveAll(List<ImageDomain> images);
}
