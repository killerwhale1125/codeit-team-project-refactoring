package com.gathering.challenge.infrastructure.entity;

import com.gathering.challenge.domain.ChallengeDomain;
import com.gathering.challengeuser.infrastructure.entity.ChallengeUser;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.domain.ReadingTimeGoal;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.persistence.Persistence.getPersistenceUtil;

@Getter
@Entity
public class Challenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @OneToMany(mappedBy = "challenge", orphanRemoval = true)
    private List<ChallengeUser> challengeUsers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ChallengeStatus challengeStatus;

    private double completeRate;    // 챌린지 전체 달성률 ( 유저 비율당 계산 예정 )
    private LocalDate startDate;
    private LocalDate endDate;  // 챌린지 종료일

    // Enum Type 변경
    @Enumerated(EnumType.STRING)
    private ReadingTimeGoal readingTimeGoal;

    public void start() {
        this.challengeStatus = ChallengeStatus.ACTIVE;
    }

    public void end() {
        this.challengeStatus = ChallengeStatus.COMPLETED;
    }

    public static Challenge fromEntity(ChallengeDomain challenge) {
        Challenge challengeEntity = new Challenge();
        challengeEntity.id = challenge.getId();
        challengeEntity.challengeStatus = challenge.getChallengeStatus();
        challengeEntity.completeRate = challenge.getCompleteRate();
        challengeEntity.startDate = challenge.getStartDate();
        challengeEntity.endDate = challenge.getEndDate();
        challengeEntity.readingTimeGoal = challenge.getReadingTimeGoal();
        return challengeEntity;
    }

    public ChallengeDomain toEntity() {
        ChallengeDomain.ChallengeDomainBuilder builder = ChallengeDomain.builder()
                .id(id)
                .challengeStatus(challengeStatus)
                .completeRate(completeRate)
                .startDate(startDate)
                .endDate(endDate)
                .readingTimeGoal(readingTimeGoal);

        if (challengeUsers != null && getPersistenceUtil().isLoaded(challengeUsers)) {
            builder.challengeUsers(challengeUsers.stream().map(ChallengeUser::toEntity).collect(Collectors.toList()));
        }

        return builder.build();
    }

}
