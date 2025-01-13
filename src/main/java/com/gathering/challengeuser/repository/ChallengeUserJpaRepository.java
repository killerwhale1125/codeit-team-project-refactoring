package com.gathering.challengeuser.repository;

import com.gathering.challengeuser.model.entity.ChallengeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChallengeUserJpaRepository extends JpaRepository<ChallengeUser, Long> {
    @Query("SELECT cu FROM ChallengeUser cu WHERE cu.challenge.id = :challengeId AND cu.user.id = :userId")
    Optional<ChallengeUser> getByChallengeIdAndUserId(@Param("challengeId") Long challengeId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE ChallengeUser cu SET cu.readingRate = :readingRate WHERE cu.id = :challengeUserId")
    void updateReadingRate(@Param("challengeUserId") Long challengeUserId, @Param("readingRate") double readingRate);

}
