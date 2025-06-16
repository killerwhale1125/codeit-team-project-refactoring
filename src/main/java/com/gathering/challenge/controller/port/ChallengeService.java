package com.gathering.challenge.controller.port;

public interface ChallengeService {
    void start(Long challengeId);

    void end(Long challengeId);
}
