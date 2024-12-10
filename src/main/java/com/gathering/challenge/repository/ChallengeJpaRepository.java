package com.gathering.challenge.repository;

import com.gathering.challenge.model.entity.Challenge;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChallengeJpaRepository extends JpaRepository<Challenge, Long> {

    @EntityGraph(attributePaths = {"challengeUsers"})
    @Query("SELECT c FROM Challenge c WHERE c.id = :challengeId")
    Optional<Challenge> getChallengeUsersById(@Param("challengeId") Long challengeId);

    @EntityGraph(attributePaths = {"gathering"})
    @Query("SELECT c FROM Challenge c WHERE c.id = :challengeId")
    Optional<Challenge> findGatheringAndChallengeById(@Param("challengeId") Long challengeId);
}
