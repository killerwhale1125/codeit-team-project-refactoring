package com.gathering.image.service;

import com.gathering.common.base.exception.BaseException;
import com.gathering.image.domain.ImageDomain;
import com.gathering.image.infrastructure.DefaultMultipartFile;
import com.gathering.image.infrastructure.entity.EntityType;
import com.gathering.image.service.port.ImageRepository;
import com.gathering.util.image.SystemFileUtils;
import com.gathering.util.string.UUIDUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gathering.common.base.response.BaseResponseStatus.FILE_UPLOAD_FAILED;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final AwsS3Service awsS3Service;
    private final ImageRepository imageRepository;

    @Override
    public List<ImageDomain> uploadImage(List<MultipartFile> files, EntityType entityType, UUIDUtils uuidUtils) {
        List<ImageDomain> images = upload(validateAndPrepareFiles(files), entityType, uuidUtils);
        return imageRepository.saveAll(images);
    }

    /**
     * 게시물에 이미지 파일 업로드
     */
    private List<ImageDomain> upload(List<MultipartFile> files, EntityType entityType, UUIDUtils uuidUtils) {
        return uploadImageToStorageServer(files, entityType, uuidUtils);
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
     * AWS S3 이미지 업로드
     */
    private List<ImageDomain> uploadImageToStorageServer(List<MultipartFile> files, EntityType entityType, UUIDUtils uuidUtils) {
        return files.stream()
                .map(file -> {
                    try {
                        // file 이름은 랜덤으로 생성
                        String filename = SystemFileUtils.getRandomFilename(uuidUtils);
                        /**
                         * File path를 가져오기 위한 과정
                         * 1. File 확장자명 유효성을 검사 하여 Filepath fullname 생성
                         * 2. 생성한 fullname으로 AWS S3에 파일 업로드
                         * 3. S3 bucket에 업로드한 해당 AWS client의 파일 fath를 가져옴
                         */
                        String filepath = awsS3Service.upload(file, filename, entityType);
                        return ImageDomain.create(filepath, filename);
                    } catch (IOException e) {
                        throw new BaseException(FILE_UPLOAD_FAILED);
                    }
                })
                .collect(Collectors.toList());
    }

//    @Transactional
//    public void delete(Long id) {
//        imageRepository.findImageByGatheringId(id).stream()
//                .forEach(image -> {
//                    try {
//                        awsS3Service.delete(image.getUrl());
//                    } catch (Exception e) {
//                        // 로깅 및 예외 처리
//                        log.error("Failed to delete image from S3: " + image.getUrl(), e);
//                    }
//                });
//
//        gatheringImageJdbcRepository.deleteAllByGatheringId(gatheringId);
//    }
}
