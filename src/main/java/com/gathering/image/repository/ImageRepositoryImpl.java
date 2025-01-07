package com.gathering.image.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.image.model.domain.ImageDomain;
import com.gathering.image.model.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public ImageDomain save(ImageDomain image) {
        return imageJpaRepository.save(Image.fromEntity(image)).toEntity();
    }

    @Override
    public List<ImageDomain> saveAll(List<ImageDomain> images) {
        return imageJpaRepository.saveAll(Image.fromEntity(images)).stream()
                .map(Image::toEntity)
                .collect(Collectors.toList());
    }
}
