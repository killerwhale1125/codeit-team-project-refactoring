package com.gathering.gatheringuser.infrastructure;

import com.gathering.gatheringuser.domain.GatheringUserStatus;
import com.gathering.gatheringuser.infrastructure.entity.GatheringUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GatheringUserJpaRepository extends JpaRepository<GatheringUser, Long> {
    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT gu FROM GatheringUser gu WHERE gu.gathering.id = :gatheringId AND gu.gatheringUserStatus = :gatheringUserStatus")
    List<GatheringUser> findByGatheringIdWithUsers(@Param("gatheringId") Long gatheringId, @Param("gatheringUserStatus") GatheringUserStatus gatheringUserStatus);
}