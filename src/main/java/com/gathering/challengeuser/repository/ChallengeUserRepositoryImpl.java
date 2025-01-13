package com.gathering.challengeuser.repository;

import com.gathering.challengeuser.model.domain.ChallengeUserDomain;
import com.gathering.challengeuser.model.entity.ChallengeUser;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.gathering.common.base.response.BaseResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class ChallengeUserRepositoryImpl implements ChallengeUserRepository {

    private final ChallengeUserJpaRepository challengeUserJpaRepository;

    @Override
    public ChallengeUserDomain save(ChallengeUserDomain challengeUserDomain) {
        ChallengeUserDomain challengeUser = challengeUserJpaRepository.save(ChallengeUser.fromEntity(challengeUserDomain)).toEntity();
        return challengeUser;
    }

    @Override
    public void deleteById(Long challengeUserId) {
        challengeUserJpaRepository.deleteById(challengeUserId);
    }

    @Override
    public void join(ChallengeUserDomain challengeUserDomain) {
        challengeUserJpaRepository.save(ChallengeUser.fromEntity(challengeUserDomain));
    }

    @Override
    public ChallengeUserDomain getByChallengeIdAndUserId(Long challengeId, Long userId) {
        return challengeUserJpaRepository.getByChallengeIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new BaseException(NON_EXISTED_CHALLENGE_USER))
                .toEntity();
    }

    @Override
    public void readBookCompleted(ChallengeUserDomain challengeUser) {
        challengeUserJpaRepository.updateReadingRate(challengeUser.getId(), challengeUser.getReadingRate());
    }
}
