package com.gathering.image.infrastructure.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.image.domain.ImageDomain;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
public class Image extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_name")
    private String name;

    @Column(name = "image_url")
    private String url;

    @Column(name = "is_removed")
    private boolean removed;

    public static Image fromEntity(ImageDomain image) {
        Image imageEntity = new Image();
        imageEntity.id = image.getId();
        imageEntity.name = image.getName();
        imageEntity.url = image.getUrl();
        imageEntity.removed = image.isRemoved();
        return imageEntity;
    }

    public static List<Image> fromEntity(List<ImageDomain> images) {
        return images.stream()
                .map(Image::fromEntity)
                .collect(Collectors.toList());
    }

    public ImageDomain toEntity() {
        return ImageDomain.builder()
                .id(id)
                .name(name)
                .url(url)
                .removed(removed)
                .build();
    }
}
