package com.gathering.image.service;

import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.repository.GatheringRepository;
import com.gathering.image.entity.EntityType;
import com.gathering.image.entity.Image;
import com.gathering.image.repository.ImageJdbcRepository;
import com.gathering.util.image.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GatheringImageUploadService {

    private final AwsS3Service awsS3Service;
    private final ImageJdbcRepository imageJdbcRepository;
    private final GatheringRepository gatheringRepository;

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
    private List<Image> uploadImageToStorageServer(List<MultipartFile> files, Gathering gathering){
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
}
