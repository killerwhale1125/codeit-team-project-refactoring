package com.gathering.challenge.service.port;

import com.gathering.challenge.domain.ChallengeDomain;
import com.gathering.challenge.infrastructure.entity.Challenge;

import java.time.LocalDate;
import java.util.List;

public interface ChallengeRepository {
    ChallengeDomain save(ChallengeDomain challenge);

    ChallengeDomain getByIdWithChallengeUsers(Long id);

    Challenge findById(Long challengeId);

    List<ChallengeDomain> getByIdsIn(List<Long> challengeIds);

    List<Long> findByStartDate(LocalDate today);

    ChallengeDomain findGatheringAndChallengeById(Long challengeId);
}
