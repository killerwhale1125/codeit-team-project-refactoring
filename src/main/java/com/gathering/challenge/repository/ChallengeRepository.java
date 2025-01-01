package com.gathering.challenge.repository;

import com.gathering.challenge.model.entity.Challenge;

import java.time.LocalDate;
import java.util.List;

public interface ChallengeRepository {
    void save(Challenge challenge);

    Challenge getChallengeUsersById(Long id);

    Challenge findById(Long challengeId);

    Challenge findGatheringAndChallengeById(Long challengeId);

    List<Long> findByStartDate(LocalDate today);
}
