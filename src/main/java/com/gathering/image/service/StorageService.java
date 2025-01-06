package com.gathering.image.service;

import com.gathering.image.model.entity.EntityType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    public String upload(MultipartFile file, String filename, EntityType entityType) throws IOException;
}
