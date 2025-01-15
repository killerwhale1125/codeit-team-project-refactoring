package com.gathering.util.image;

import com.gathering.common.base.exception.BaseException;
import com.gathering.image.infrastructure.entity.EntityType;
import com.gathering.util.string.UUIDUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Objects;

import static com.gathering.common.base.response.BaseResponseStatus.UNSUPPORTED_FILETYPE;

public class SystemFileUtils implements FileUtils {

    private static final String BASE_DIRECTORY = "image";

    public static String getRandomFilename(UUIDUtils uuidUtils) {
        return uuidUtils.getRandom().replace("-", "");
    }

    public static String getFilePath(MultipartFile file, String filename, EntityType entityType, String uploadPath) {
        /**
         * File 확장자명 ex ) jpeg, png 등
         */
        String extension = StringUtils.getFilenameExtension(Objects.requireNonNull(file.getOriginalFilename()));

        if(!isValidFileType(extension)) {
            throw new BaseException(UNSUPPORTED_FILETYPE);
        }

        return uploadPath + BASE_DIRECTORY + "/" + entityType.getEntityType() + "/" + filename + "." + extension;
    }

    /**
     * File 확장자 유효성 검사
     */
    private static boolean isValidFileType(String extension) {
        return Arrays.stream(FileType.values())
                .anyMatch(type -> type.getExtension().equals(extension));
    }
}
