package com.gathering.mock.fake.service;

import com.gathering.common.base.exception.BaseException;
import com.gathering.image.model.domain.ImageDomain;
import com.gathering.image.model.entity.EntityType;
import com.gathering.image.repository.ImageRepository;
import com.gathering.image.service.ImageService;
import com.gathering.image.service.StorageService;
import com.gathering.mock.test.TestFileUtils;
import com.gathering.util.string.UUIDUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.gathering.common.base.response.BaseResponseStatus.FILE_UPLOAD_FAILED;

public class FakeImageService implements ImageService {

    private final StorageService storageService;
    private final ImageRepository imageRepository;

    public FakeImageService(StorageService storageService, ImageRepository imageRepository) {
        this.storageService = storageService;
        this.imageRepository = imageRepository;
    }

    @Override
    public List<ImageDomain> uploadImage(List<MultipartFile> files, EntityType entityType, UUIDUtils uuidUtils) {
        return files.stream()
                .map(file -> {
                    try {
                        String filename = TestFileUtils.getRandomFilename(uuidUtils);
                        String filepath = storageService.upload(file, filename, entityType);
                        return imageRepository.save(ImageDomain.create(filepath, filename));
                    } catch (IOException e) {
                        throw new BaseException(FILE_UPLOAD_FAILED);
                    }
                })
                .collect(Collectors.toList());
    }
}
