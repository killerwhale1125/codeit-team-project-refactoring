package com.gathering.image.repository;

import com.gathering.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i JOIN FETCH i.gathering g WHERE g.id = :gatheringId")
    List<Image> findImageByGatheringId(@Param("gatheringId") Long gatheringId);
}
