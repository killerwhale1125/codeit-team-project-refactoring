package com.gathering.image.service;

import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.repository.GatheringRepository;
import com.gathering.image.entity.EntityType;
import com.gathering.image.entity.Image;
import com.gathering.image.repository.ImageJdbcRepository;
import com.gathering.image.repository.ImageRepository;
import com.gathering.util.image.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatheringImageService {

    private final AwsS3Service awsS3Service;
    private final ImageJdbcRepository imageJdbcRepository;
    private final GatheringRepository gatheringRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public void upload(Long gatheringId, List<MultipartFile> files) throws IOException {
        Gathering gathering = gatheringRepository.getById(gatheringId);
        upload(files, gathering);
    }

    /**
     * 게시물에 이미지 파일 업로드
     */
    private void upload(List<MultipartFile> files, Gathering gathering) {
        List<Image> images = uploadImageToStorageServer(files, gathering);
        imageJdbcRepository.saveAll(images);
    }

    /**
     * AWS S3 이미지 업로드
     */
    private List<Image> uploadImageToStorageServer(List<MultipartFile> files, Gathering gathering) {
        return files.stream()
                .map(file -> {
                    try {
                        // file 이름은 랜덤으로 생성
                        String filename = FileUtils.getRandomFilename();
                        /**
                         * File path를 가져오기 위한 과정
                         * 1. File 확장자명 유효성을 검사 하여 Filepath fullname 생성
                         * 2. 생성한 fullname으로 AWS S3에 파일 업로드 
                         * 3. S3 bucket에 업로드한 해당 AWS client의 파일 fath를 가져옴
                         */
                        String filepath = awsS3Service.upload(file, filename, EntityType.GATHERING);
                        return Image.builder()
                                .name(filename)
                                .url(filepath)
                                .gathering(gathering)
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload file", e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long gatheringId) {
        imageRepository.findImageByGatheringId(gatheringId).stream()
                .forEach(image -> {
                    try {
                        awsS3Service.delete(image.getUrl());
                    } catch (Exception e) {
                        // 로깅 및 예외 처리
                        log.error("Failed to delete image from S3: " + image.getUrl(), e);
                    }
                });

        imageJdbcRepository.deleteAllByGatheringId(gatheringId);
    }
}
