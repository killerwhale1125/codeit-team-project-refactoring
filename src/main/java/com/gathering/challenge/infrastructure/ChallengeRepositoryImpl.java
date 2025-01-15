package com.gathering.challenge.infrastructure;

import com.gathering.challenge.domain.ChallengeDomain;
import com.gathering.challenge.infrastructure.entity.Challenge;
import com.gathering.challenge.service.port.ChallengeRepository;
import com.gathering.common.base.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<ChallengeDomain> getByIdsIn(List<Long> challengeIds) {
        return challengeJpaRepository.getByIdsIn(challengeIds).stream().map(Challenge::toEntity).collect(Collectors.toList());
    }

}
