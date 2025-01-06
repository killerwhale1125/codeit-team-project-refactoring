package com.gathering.image.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static Image createImage(String filepath, String filename) {
        Image image = new Image();
        image.name = filename;
        image.url = filepath;
        return image;
    }
}
