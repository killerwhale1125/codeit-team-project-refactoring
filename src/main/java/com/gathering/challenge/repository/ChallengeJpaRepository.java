package com.gathering.challenge.repository;

import com.gathering.challenge.model.entity.Challenge;
import com.gathering.challengeuser.model.entity.ChallengeUser;
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
    Optional<Challenge> getChallengeUsersById(@Param("challengeId") Long challengeId);

    @EntityGraph(attributePaths = {"gathering", "gathering.gatheringUsers"})
    @Query("SELECT c FROM Challenge c WHERE c.id = :challengeId")
    Optional<Challenge> findGatheringAndChallengeById(@Param("challengeId") Long challengeId);

    @Query("SELECT c.id FROM Challenge c WHERE c.startDateTime = :today")
    List<Long> findByStartDate(@Param("today") LocalDate today);

    @Query("SELECT cu FROM Challenge c INNER JOIN c.challengeUsers cu WHERE c.id = :challengeId AND cu.user.id = :userId")
    Optional<ChallengeUser> getChallengeUserByChallengeIdAndUserId(@Param("challengeId") Long challengeId, @Param("userId") Long userId);

    @EntityGraph(attributePaths = {"challengeUsers", "challengeUsers.user"})
    @Query("SELECT c FROM Challenge c WHERE c.id IN :challengeIds")
    List<Challenge> getByIdsIn(List<Long> challengeIds);

}
