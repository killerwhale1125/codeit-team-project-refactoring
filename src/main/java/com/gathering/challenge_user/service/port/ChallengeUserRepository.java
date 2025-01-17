package com.gathering.challenge_user.service.port;

import com.gathering.challenge_user.domain.ChallengeUserDomain;

public interface ChallengeUserRepository {
    ChallengeUserDomain save(ChallengeUserDomain challengeUserDomain);

    void deleteById(Long challengeUserId);

    void join(ChallengeUserDomain challengeUserDomain);

    ChallengeUserDomain getByChallengeIdAndUserId(Long challengeId, Long userId);

    void readBookCompleted(ChallengeUserDomain challengeUser);

}
