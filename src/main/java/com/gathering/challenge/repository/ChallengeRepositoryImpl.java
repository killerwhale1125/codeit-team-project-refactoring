package com.gathering.challenge.repository;

import com.gathering.challenge.model.entity.Challenge;
import com.gathering.common.base.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.gathering.common.base.response.BaseResponseStatus.NON_EXISTED_CHALLENGE;

@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepository {

    private final ChallengeJpaRepository challengeJpaRepository;

    @Override
    public void save(Challenge challenge) {
        challengeJpaRepository.save(challenge);
    }

    @Override
    public Challenge getChallengeUsersById(Long challengeId) {
        return challengeJpaRepository.getChallengeUsersById(challengeId).orElseThrow(() -> new BaseException(NON_EXISTED_CHALLENGE));
    }
}
