package com.gathering.challengeuser.repository;

import com.gathering.challengeuser.model.domain.ChallengeUserDomain;

public interface ChallengeUserRepository {
    ChallengeUserDomain save(ChallengeUserDomain challengeUserDomain);
}
