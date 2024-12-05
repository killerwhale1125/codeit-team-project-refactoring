package com.gathering.util.file;

import com.gathering.common.model.entity.Attachment;
import com.gathering.common.repository.AttachmentJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileUtil {

    private final AttachmentJpaRepository attachmentJpaRepository;

    private static final String dot = ".";
    private static final List<String> ALLOWED_IMG_EXTENSIONS = Arrays.asList("jpg", "png");

    public String FileUpload(String serverPath, MultipartFile file, String code, long userId) {

        File uploadDir = new File(serverPath);

        // 폴더가 없을경우 폴더 생성
        if(!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String uuid = String.valueOf(UUID.randomUUID());
        String extension = getFileExtension(file);

        if (!file.getOriginalFilename().contains(dot) || !ALLOWED_IMG_EXTENSIONS.contains(extension)) {
            throw new  RuntimeException();
        }
        String fileName = uuid + dot + extension;

        File destinationFile = new File(uploadDir,fileName);
        try ( InputStream inputStream = file.getInputStream();
              OutputStream outputStream = new FileOutputStream(destinationFile);
        ) {

            int readByte = 0;
            byte[] buffer = new byte[8192];

            while ((readByte = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readByte);
            }
            System.out.println("파일 생성 완료");

            Attachment attachment = Attachment.CreateAttachment(file, code, serverPath+fileName, uuid, userId);
            attachmentJpaRepository.save(attachment);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return serverPath+fileName;

    }

    // 확장자 추출
    public static String getFileExtension(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }


    // 파일 삭제
    @Transactional
    public void DeleteFile(String filePath) {

        try {
            // 파일 경로를 Path 객체로 변환
            Path path = Paths.get(filePath);

            // 파일이 존재하면 삭제
            if (Files.exists(path)) {
                Files.delete(path);
                attachmentJpaRepository.deleteByPath(filePath);

            }
        } catch (IOException e) {
            // 파일 삭제 중 예외 처리
            throw new RuntimeException(e);
        }
    }
}
