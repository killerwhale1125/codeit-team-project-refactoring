package com.gathering.challenge.repository;

import com.gathering.challenge.model.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeJpaRepository extends JpaRepository<Challenge, Long> {
}
