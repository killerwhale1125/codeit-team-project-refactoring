package com.gathering.gathering.repository;

import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUser;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GatheringJpaRepository extends JpaRepository<Gathering, Long> {
    @Query("SELECT g FROM Gathering g " +
            "JOIN FETCH g.gatheringUsers gu " +
            "JOIN FETCH gu.user u " +
            "WHERE g.id = :gatheringId " +
            "AND gu.gatheringUserStatus = :gatheringUserStatus")
    Optional<Gathering> findGatheringWithUsersByIdAndStatus(@Param("gatheringId") Long gatheringId, @Param("gatheringUserStatus") GatheringUserStatus gatheringUserStatus);
}
