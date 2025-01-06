package com.gathering.image.model;

import com.gathering.common.base.exception.BaseException;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.gathering.common.base.response.BaseResponseStatus.INTERNAL_SERVER_ERROR;

/**
 * 기본 이미지 파일용
 */
@Getter
public class DefaultMultipartFile implements MultipartFile {
    private final byte[] content;
    private final String name;
    private final String originalFilename;
    private final String contentType;

    public DefaultMultipartFile(byte[] content, String name, String originalFilename, String contentType) {
        this.content = content;
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    public static MultipartFile createDefaultImage() {
        ClassPathResource resource = new ClassPathResource("static/images/default.jpg");
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] content = IOUtils.toByteArray(inputStream);
            return new DefaultMultipartFile(
                    content,
                    "default.jpg",
                    resource.getFilename(),
                    "image/jpeg"
            );
        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean isEmpty() {
        return content == null || content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length; // 파일 크기 반환
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content; // 파일 내용을 바이트 배열로 반환
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content); // 바이트 배열을 InputStream으로 반환
    }

    @Override
    public Resource getResource() {
        return new ByteArrayResource(content); // Resource로 반환
    }

    @Override
    public void transferTo(File dest) throws IOException {
        // 파일을 지정된 경로로 저장
        try (FileOutputStream out = new FileOutputStream(dest)) {
            out.write(content);
        }
    }

    @Override
    public void transferTo(Path dest) throws IOException {
        // 파일을 지정된 Path 경로로 저장
        Files.write(dest, content);
    }
}
