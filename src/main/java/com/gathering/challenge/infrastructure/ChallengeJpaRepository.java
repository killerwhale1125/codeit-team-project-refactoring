package com.gathering.challenge.infrastructure;

import com.gathering.challenge.infrastructure.entity.Challenge;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeJpaRepository extends JpaRepository<Challenge, Long> {

    @EntityGraph(attributePaths = {"challengeUsers"})
    @Query("SELECT c FROM Challenge c WHERE c.id = :challengeId")
    Optional<Challenge> getByIdWithChallengeUsers(@Param("challengeId") Long challengeId);

    @EntityGraph(attributePaths = {"challengeUsers", "challengeUsers.user"})
    @Query("SELECT c FROM Challenge c WHERE c.id IN :challengeIds")
    List<Challenge> getByIdsIn(List<Long> challengeIds);

    @Query("SELECT c.id FROM Challenge c WHERE c.startDate >= :today")
    List<Long> findByStartDate(@Param("today") LocalDate today);

    @EntityGraph(attributePaths = {"gathering"})
    @Query("SELECT c FROM Challenge c WHERE c.id = :challengeId")
    Challenge findGatheringAndChallengeById(Long challengeId);
}
