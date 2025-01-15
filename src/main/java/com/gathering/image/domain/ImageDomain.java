package com.gathering.image.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageDomain {
    private Long id;
    private String name;
    private String url;
    private boolean removed;

    public static ImageDomain create(String filepath, String filename) {
        return ImageDomain.builder()
                .name(filename)
                .url(filepath)
                .removed(false)
                .build();
    }
}
