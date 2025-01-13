package com.gathering.challenge.repository;

import com.gathering.challenge.model.domain.ChallengeDomain;
import com.gathering.challenge.model.entity.Challenge;
import com.gathering.challengeuser.model.domain.ChallengeUserDomain;
import com.gathering.challengeuser.model.entity.ChallengeUser;

import java.time.LocalDate;
import java.util.List;

public interface ChallengeRepository {
    ChallengeDomain save(ChallengeDomain challenge);

    ChallengeDomain getByIdWithChallengeUsers(Long id);

    Challenge findById(Long challengeId);

    List<Challenge> getByIdsIn(List<Long> challengeIds);

}
