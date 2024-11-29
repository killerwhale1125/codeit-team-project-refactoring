package com.gathering.challenge.repository;

import com.gathering.challenge.model.entity.ChallengeUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeUserJpaRepository extends JpaRepository<ChallengeUser, Long> {
}
