package com.gathering.challengeuser.service.port;

import com.gathering.challengeuser.domain.ChallengeUserDomain;

public interface ChallengeUserRepository {
    ChallengeUserDomain save(ChallengeUserDomain challengeUserDomain);

    void deleteById(Long challengeUserId);

    void join(ChallengeUserDomain challengeUserDomain);

    ChallengeUserDomain getByChallengeIdAndUserId(Long challengeId, Long userId);

    void readBookCompleted(ChallengeUserDomain challengeUser);

}
