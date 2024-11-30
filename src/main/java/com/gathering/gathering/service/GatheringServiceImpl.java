package com.gathering.gathering.service;

import com.gathering.challenge.model.entity.Challenge;
import com.gathering.challenge.model.entity.ChallengeUser;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.GatheringUser;
import com.gathering.gathering.repository.GatheringRepository;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.repository.UserRepository;
import com.gathering.util.holder.DateCalculateHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GatheringServiceImpl implements GatheringService {

    private final GatheringRepository gatheringRepository;
    private final UserRepository userRepository;
    private final DateCalculateHolder dateCalculateHolder;

    /**
     * 모임 생성
     */
    @Override
    @Transactional
    public void create(GatheringCreate gatheringCreate, UserDetails userDetails) {
        User user = userRepository.findByUsername("test");

        // Challenge 생성 ( 양방향 관계 설정 )
        ChallengeUser challengeUser = ChallengeUser.createChallengeUser(user);
        Challenge challenge = Challenge.createChallenge(gatheringCreate.getEndDate(), challengeUser);

        // Gathering 생성 ( 양방향 관계 설정 )
        GatheringUser gatheringUser = GatheringUser.createGatheringUser(user);
        Gathering gathering = Gathering.createGathering(gatheringCreate, challenge, gatheringUser, dateCalculateHolder);

        gatheringRepository.save(gathering);
    }
}
