package com.gathering.challengeuser.repository;

import com.gathering.challengeuser.model.domain.ChallengeUserDomain;
import com.gathering.challengeuser.model.entity.ChallengeUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
