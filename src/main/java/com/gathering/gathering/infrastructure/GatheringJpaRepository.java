package com.gathering.gathering.infrastructure;

import com.gathering.gathering.infrastructure.entity.Gathering;
import com.gathering.gathering.domain.GatheringStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GatheringJpaRepository extends JpaRepository<Gathering, Long> {
    @EntityGraph(attributePaths = {"gatheringUsers", "gatheringUsers.user", "challenge"})
    @Query("SELECT g FROM Gathering g WHERE g.id = :gatheringId")
    Optional<Gathering> findByIdWithGatheringUsersAndChallenge(@Param("gatheringId") Long gatheringId);

    @EntityGraph(attributePaths = {"gatheringUsers", "gatheringUsers.user", "challenge"})
    @Query("SELECT g FROM Gathering g WHERE g.id = :gatheringId")
    Optional<Gathering> getByIdWithGatheringUsersAndChallenge(@Param("gatheringId") Long gatheringId);

    List<Gathering> findByIdIn(List<Long> list);

    @Query("SELECT g.id FROM Gathering g WHERE g.id = :gatheringId")
    Optional<Long> findIdById(@Param("gatheringId") Long gatheringId);

    @Query("SELECT COUNT(g.id) FROM Gathering g " +
            "LEFT JOIN g.gatheringUsers gu " +
            "WHERE gu.user.id = :userId " +
            "AND gu.gatheringUserStatus = PARTICIPATING " +
            "AND g.gatheringStatus != DELETED " +
            "AND g.gatheringStatus != COMPLETED")
    long getActiveAndParticipatingCountByUserId(@Param("userId") long userId);

    @Query("SELECT COUNT(g.id) FROM Gathering g " +
            "LEFT JOIN g.gatheringUsers gu " +
            "WHERE gu.user.id = :userId " +
            "AND gu.gatheringUserStatus = NOT_PARTICIPATING " +
            "AND g.gatheringStatus = COMPLETED")
    long getCompletedCountByUserId(@Param("userId") long userId);

    @Query("SELECT COUNT(g.id) FROM Gathering g WHERE g.owner = :username")
    long getMyCreatedCountByUserName(@Param("username") String username);

    @Query("SELECT COUNT(g.id) FROM Gathering g WHERE g.id IN :wishGatheringIds")
    long getMyWishedCountByGatheringIds(@Param("wishGatheringIds") Set<Long> wishGatheringIds);

    @EntityGraph(attributePaths = {"gatheringUsers"})
    @Query("SELECT g FROM Gathering g WHERE g.id = :gatheringId")
    Optional<Gathering> findByIdWithGatheringUsers(@Param("gatheringId") long gatheringId);

    @EntityGraph(attributePaths = {"book", "challenge"})
    @Query("SELECT g FROM Gathering g WHERE g.id = :gatheringId")
    Optional<Gathering> findByIdWithBookAndChallenge(@Param("gatheringId") Long gatheringId);

    @Modifying
    @Query("UPDATE Gathering g SET g.currentCapacity = :currentCapacity, g.gatheringStatus = :gatheringStatus WHERE g.id = :gatheringId")
    void updateCurrentCapacityAndStatus(@Param("gatheringId") Long gatheringId,
                                        @Param("currentCapacity") int currentCapacity,
                                        @Param("gatheringStatus") GatheringStatus gatheringStatus);
}
