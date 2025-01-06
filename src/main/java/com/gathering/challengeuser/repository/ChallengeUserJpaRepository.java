package com.gathering.challengeuser.repository;

import com.gathering.challengeuser.model.entity.ChallengeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeUserJpaRepository extends JpaRepository<ChallengeUser, Long> {
}
