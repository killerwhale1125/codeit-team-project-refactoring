package com.gathering.gathering.repository;

import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUserStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GatheringJpaRepository extends JpaRepository<Gathering, Long> {
    @Query("SELECT g FROM Gathering g " +
            "JOIN FETCH g.gatheringUsers gu " +
            "JOIN FETCH g.challenge c " +
            "WHERE g.id = :gatheringId " +
            "AND gu.gatheringUserStatus = :gatheringUserStatus")
    Optional<Gathering> findGatheringWithUsersByIdAndStatus(@Param("gatheringId") Long gatheringId, @Param("gatheringUserStatus") GatheringUserStatus gatheringUserStatus);

    @EntityGraph(attributePaths = {"gatheringUsers"})
    @Query("SELECT g FROM Gathering g WHERE g.id = :gatheringId")
    Optional<Gathering> getGatheringAndGatheringUsersById(@Param("gatheringId") Long gatheringId);

}
