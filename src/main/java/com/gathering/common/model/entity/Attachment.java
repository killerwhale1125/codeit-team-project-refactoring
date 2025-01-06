package com.gathering.common.model.entity;

import com.gathering.common.base.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.web.multipart.MultipartFile;

import static com.gathering.util.file.FileUtil.getFileExtension;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    private long creatorId;

    public static Attachment CreateAttachment(MultipartFile file, String code, String path, String serverFileName, long creatorId) {
        return Attachment.builder()
                .code(code)
                .path(path)
                .fileName(serverFileName)
                .originalName(file.getOriginalFilename())
                .extension(getFileExtension(file))
                .creatorId(creatorId)
                .build();
    }
}
