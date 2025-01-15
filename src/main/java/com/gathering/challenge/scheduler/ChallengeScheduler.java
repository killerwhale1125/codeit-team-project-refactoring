package com.gathering.challenge.scheduler;

import com.gathering.challenge.service.port.ChallengeRepository;
import com.gathering.challenge.controller.port.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChallengeScheduler {

    private final ChallengeService challengeService;
    private final ChallengeRepository challengeRepository;

//    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 12시 실행 ( 초 분 시 일 월 요일 [년] )
//    public void startChallenge() {
//        // 1. 오늘 날짜 가져오기
//        LocalDate today = LocalDate.now();
//
//        // 2. 오늘 시작되는 챌린지 조회한다
//        List<Long> challengeIds = challengeRepository.findByStartDate(today);
//
//        // 3. 조회된 챌린지에 대한 챌린지 및 모임 상태 변경한다
//        for (Long challengeId : challengeIds) {
//            challengeService.start(challengeId);
//            log.info("update completed challenge status challengeId = {}", challengeId);
//        }
//
//    }

//    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 12시 실행 ( 초 분 시 일 월 요일 [년] )
//    public void endChallenge() {
//        // 1. 어제 날짜를 계산한다
//        LocalDate yesterday = LocalDate.now().minusDays(1);
//
//        // 2. 어제 종료된 챌린지를 조회한다
//        List<Long> challengeIds = challengeRepository.findByStartDate(yesterday);
//
//        // 3. 조회된 챌린지의 상태를 종료 상태로 변경한다.
//        for (Long challengeId : challengeIds) {
//            challengeService.end(challengeId);
//            log.info("update completed challenge status challengeId = {}", challengeId);
//        }
//
//    }
}
