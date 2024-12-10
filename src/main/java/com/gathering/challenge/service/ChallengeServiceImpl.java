package com.gathering.challenge.service;

import com.gathering.challenge.model.entity.Challenge;
import com.gathering.challenge.repository.ChallengeRepository;
import com.gathering.gathering.model.entity.Gathering;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Override
    @Transactional
    public void start(Long challengeId) {
        Challenge challenge = challengeRepository.findGatheringAndChallengeById(challengeId);
        challenge.start();
        Gathering gathering = challenge.getGathering();
        gathering.start();
    }
}
