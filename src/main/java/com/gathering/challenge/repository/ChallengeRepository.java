package com.gathering.challenge.repository;

import com.gathering.challenge.model.entity.Challenge;

public interface ChallengeRepository {
    void save(Challenge challenge);

    Challenge getChallengeUsersById(Long id);

    Challenge findById(Long challengeId);

    Challenge findGatheringAndChallengeById(Long challengeId);
}
