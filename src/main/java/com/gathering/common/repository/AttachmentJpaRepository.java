package com.gathering.common.repository;

import com.gathering.common.model.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachmentJpaRepository extends JpaRepository<Attachment, Long> {
    public Optional<Attachment> findByPath(String path);

    public void deleteByPath(String filePath);
}
