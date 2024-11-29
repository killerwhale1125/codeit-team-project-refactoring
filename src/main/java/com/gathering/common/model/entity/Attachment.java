package com.gathering.common.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.util.Date;

@Entity
public class Attachment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    @Comment("파일 ID")
    private long id;
    @Comment("파일 코드")
    private String code;
    @Comment("파일 경로")
    private String path;
    @Comment("원본파일명")
    private String originalName;
    @Comment("파일명")
    private String fileName;
    @Comment("확장자")
    private String extension;

    @Comment("생성자")
    private String creator_id;
    
}
