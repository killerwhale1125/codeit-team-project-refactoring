package com.gathering.challenge.service;

import com.gathering.challenge.controller.port.ChallengeService;
import com.gathering.challenge.domain.ChallengeDomain;
import com.gathering.challenge.service.port.ChallengeRepository;
import com.gathering.gathering.domain.GatheringDomain;
import com.gathering.gathering.infrastructure.entity.Gathering;
import com.gathering.gathering.service.port.GatheringRepository;
import com.gathering.gathering_user.service.port.GatheringUserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Builder
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final GatheringRepository gatheringRepository;
    private final GatheringUserRepository gatheringUserRepository;

    @Override
    @Transactional
    public void start(Long challengeId) {
        ChallengeDomain challenge = challengeRepository.findGatheringAndChallengeById(challengeId);
        challenge.start();
        GatheringDomain gathering = challenge.getGathering();
        gathering.start();
        challengeRepository.save(challenge);
        gatheringRepository.save(gathering);
    }

    @Override
    public void end(Long challengeId) {
        ChallengeDomain challenge = challengeRepository.findGatheringAndChallengeById(challengeId);
        challenge.end();
        GatheringDomain gathering = challenge.getGathering();
        gathering.end();
        challengeRepository.save(challenge);
        gatheringRepository.save(gathering);
    }

}
