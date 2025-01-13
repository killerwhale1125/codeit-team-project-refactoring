package com.gathering.challenge.service;

import com.gathering.challenge.model.domain.ChallengeDomain;
import com.gathering.challenge.model.entity.Challenge;
import com.gathering.challenge.repository.ChallengeRepository;
import com.gathering.gathering.model.domain.GatheringDomain;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.repository.GatheringRepository;
import com.gathering.gatheringuser.model.domain.GatheringUserDomain;
import com.gathering.gatheringuser.repository.GatheringUserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
