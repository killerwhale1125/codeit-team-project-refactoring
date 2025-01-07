package com.gathering.mock.fake.service;

import com.gathering.common.base.exception.BaseException;
import com.gathering.image.model.entity.EntityType;
import com.gathering.image.service.StorageService;
import com.gathering.util.image.FileType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.gathering.common.base.response.BaseResponseStatus.UNSUPPORTED_FILETYPE;

public class FakeAwsS3Service implements StorageService {

    private final static String BASE_DIRECTORY = "http://www.codeit.com/image";

    @Override
    public String upload(MultipartFile file, String filename, EntityType entityType) throws IOException {

        String extension = StringUtils.getFilenameExtension(Objects.requireNonNull(file.getOriginalFilename()));

        if(!isValidFileType(extension)) {
            throw new BaseException(UNSUPPORTED_FILETYPE);
        }

        return BASE_DIRECTORY + "/" + entityType.getEntityType() + "/" + filename + "." + extension;
    }

    /**
     * File 확장자 유효성 검사
     */
    private static boolean isValidFileType(String extension) {
        return Arrays.stream(FileType.values())
                .anyMatch(type -> type.getExtension().equals(extension));
    }
}
