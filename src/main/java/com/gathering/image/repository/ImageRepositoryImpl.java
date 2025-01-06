package com.gathering.image.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.image.model.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gathering.common.base.response.BaseResponseStatus.NON_EXISTED_IMAGE;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {

    private final ImageJpaRepository imageJpaRepository;

    @Override
    public Image findById(Long imageId) {
        return imageJpaRepository.findById(imageId).orElseThrow(() -> new BaseException(NON_EXISTED_IMAGE));
    }

    @Override
    public void delete(Image image) {
        imageJpaRepository.delete(image);
    }

    @Override
    public List<Image> findImageByGatheringId(Long gatheringId) {
//        return imageJpaRepository.findImageByGatheringId(gatheringId);
        return null;
    }
}
