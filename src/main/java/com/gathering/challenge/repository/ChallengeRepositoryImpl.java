package com.gathering.challenge.repository;

import com.gathering.challenge.model.domain.ChallengeDomain;
import com.gathering.challenge.model.entity.Challenge;
import com.gathering.challengeuser.model.entity.ChallengeUser;
import com.gathering.common.base.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.gathering.common.base.response.BaseResponseStatus.NON_EXISTED_CHALLENGE;

@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepository {

    private final ChallengeJpaRepository challengeJpaRepository;

    @Override
    public ChallengeDomain save(ChallengeDomain challenge) {
        return challengeJpaRepository.save(Challenge.fromEntity(challenge)).toEntity();
    }

    @Override
    public ChallengeDomain getByIdWithChallengeUsers(Long challengeId) {
        return challengeJpaRepository.getByIdWithChallengeUsers(challengeId)
                .orElseThrow(() -> new BaseException(NON_EXISTED_CHALLENGE))
                .toEntity();
    }

    @Override
    public Challenge findById(Long challengeId) {
        return challengeJpaRepository.findById(challengeId).orElseThrow(() -> new BaseException(NON_EXISTED_CHALLENGE));
    }

    @Override
    public ChallengeDomain findGatheringAndChallengeById(Long challengeId) {
        return challengeJpaRepository.findGatheringAndChallengeById(challengeId)
                .orElseThrow(() -> new BaseException(NON_EXISTED_CHALLENGE))
                .toEntity();
    }

    @Override
    public List<Long> findByStartDate(LocalDate today) {
        return challengeJpaRepository.findByStartDate(today);
    }

    @Override
    public ChallengeUser getChallengeUserByChallengeIdAndUserId(Long challengeId, long userId) {
        return challengeJpaRepository.getChallengeUserByChallengeIdAndUserId(challengeId, userId).orElseThrow(() -> new BaseException(NON_EXISTED_CHALLENGE));
    }

    @Override
    public List<Challenge> getByIdsIn(List<Long> challengeIds) {
        return challengeJpaRepository.getByIdsIn(challengeIds);
    }

}
