package com.gathering.image.service.gathering;

import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.repository.GatheringRepository;
import com.gathering.image.model.DefaultMultipartFile;
import com.gathering.image.model.entity.EntityType;
import com.gathering.image.model.entity.Image;
import com.gathering.image.repository.gathering.GatheringImageJdbcRepository;
import com.gathering.image.repository.ImageRepository;
import com.gathering.image.service.AwsS3Service;
import com.gathering.util.image.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gathering.common.base.response.BaseResponseStatus.FILE_UPLOAD_FAILED;
import static com.gathering.common.base.response.BaseResponseStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatheringImageService {

    private final AwsS3Service awsS3Service;
    private final GatheringImageJdbcRepository gatheringImageJdbcRepository;
    private final ImageRepository imageRepository;
    private final GatheringRepository gatheringRepository;

    @Transactional
    public void uploadGatheringImage(Long gatheringId, List<MultipartFile> files) {
        Gathering gathering = gatheringRepository.getById(gatheringId);
        upload(validateAndPrepareFiles(files), gathering);
    }

    private List<MultipartFile> validateAndPrepareFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            List<MultipartFile> defaultFiles = new ArrayList<>();
            defaultFiles.add(DefaultMultipartFile.createDefaultImage());
            return defaultFiles;
        }
        return files;
    }

    /**
     * 게시물에 이미지 파일 업로드
     */
    private void upload(List<MultipartFile> files, Gathering gathering) {
        List<Image> images = uploadImageToStorageServer(files, gathering);
        gatheringImageJdbcRepository.saveAll(images);
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
                        return Image.createImage(filepath, filename, gathering);
                    } catch (IOException e) {
                        throw new BaseException(FILE_UPLOAD_FAILED);
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

        gatheringImageJdbcRepository.deleteAllByGatheringId(gatheringId);
    }
}
