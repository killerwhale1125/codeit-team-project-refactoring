package com.gathering.image.infrastructure;

import com.gathering.image.infrastructure.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageJpaRepository extends JpaRepository<Image, Long> {

//    @Query("SELECT i FROM Image i JOIN FETCH i.gathering g WHERE g.id = :gatheringId")
//    List<Image> findImageByGatheringId(@Param("gatheringId") Long gatheringId);
}
