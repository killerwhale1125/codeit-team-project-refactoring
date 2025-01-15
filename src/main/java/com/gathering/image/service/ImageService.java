package com.gathering.image.service;

import com.gathering.image.domain.ImageDomain;
import com.gathering.image.infrastructure.entity.EntityType;
import com.gathering.util.string.UUIDUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    List<ImageDomain> uploadImage(List<MultipartFile> files, EntityType entityType, UUIDUtils uuidUtils);
}
