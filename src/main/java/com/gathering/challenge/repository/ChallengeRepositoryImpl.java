package com.gathering.challenge.repository;

import com.gathering.challenge.model.entity.Challenge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepository {

    private final ChallengeJpaRepository challengeJpaRepository;

    @Override
    public void save(Challenge challenge) {
        challengeJpaRepository.save(challenge);
    }
}
