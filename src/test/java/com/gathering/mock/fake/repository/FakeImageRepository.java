package com.gathering.mock.fake.repository;

import com.gathering.image.model.domain.ImageDomain;
import com.gathering.image.model.entity.Image;
import com.gathering.image.repository.ImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakeImageRepository implements ImageRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<ImageDomain> data = new ArrayList<>();

    @Override
    public Image findById(Long imageId) {
        return null;
    }

    @Override
    public void delete(Image image) {

    }

    @Override
    public List<Image> findImageByGatheringId(Long gatheringId) {
        return null;
    }

    public ImageDomain save(ImageDomain image) {
        if (Objects.isNull(image.getId())) {
            final ImageDomain createImage = ImageDomain.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .name(image.getName())
                    .url(image.getUrl())
                    .removed(false)
                    .build();
            data.add(createImage);
            return createImage;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), image.getId()));
            data.add(image);
            return image;
        }
    }

    @Override
    public List<ImageDomain> saveAll(List<ImageDomain> images) {
        return null;
    }
}