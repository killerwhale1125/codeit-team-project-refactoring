package com.gathering.challenge.service;

import com.gathering.challenge.controller.port.ChallengeService;
import com.gathering.challenge.service.port.ChallengeRepository;
import com.gathering.gathering.service.port.GatheringRepository;
import com.gathering.gathering_user.service.port.GatheringUserRepository;
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

//    @Override
//    @Transactional
//    public void start(Long challengeId) {
//        Challenge challenge = challengeRepository.findGatheringAndChallengeById(challengeId);
//        challenge.start();
//        Gathering gathering = challenge.getGathering();
//        gathering.start();
//    }

}
