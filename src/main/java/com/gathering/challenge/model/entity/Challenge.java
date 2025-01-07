package com.gathering.challenge.model.entity;

import com.gathering.challenge.model.domain.ChallengeDomain;
import com.gathering.challengeuser.model.domain.ChallengeUserDomain;
import com.gathering.challengeuser.model.entity.ChallengeUser;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.entity.Gathering;
import com.gathering.gathering.model.entity.ReadingTimeGoal;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.entitiy.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gathering.challenge.model.entity.ChallengeStatus.ACTIVE;
import static com.gathering.challenge.model.entity.ChallengeStatus.COMPLETED;
import static com.gathering.common.base.response.BaseResponseStatus.USER_NOT_IN_CHALLENGE;

@Getter
@Entity
public class Challenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @OneToMany(mappedBy = "challenge")
    private List<ChallengeUser> challengeUsers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ChallengeStatus challengeStatus;

    private double completeRate;    // 챌린지 전체 달성률 ( 유저 비율당 계산 예정 )
    private LocalDate startDate;
    private LocalDate endDate;  // 챌린지 종료일

    // Enum Type 변경
    @Enumerated(EnumType.STRING)
    private ReadingTimeGoal readingTimeGoal;

    public static void leave(Challenge challenge, UserDomain user) {
        List<ChallengeUser> challengeUsers = challenge.getChallengeUsers();
        ChallengeUser challengeUser = challengeUsers.stream()
                .filter(cu -> cu.getUser().getId() == user.getId())
                .findFirst()
                .orElseThrow(() -> new BaseException(USER_NOT_IN_CHALLENGE));
        challengeUsers.remove(challengeUser);
    }

    public static void join(Challenge challenge, ChallengeUser challengeUser) {
        challenge.addChallengeUser(challengeUser);
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

    public void start() {
        this.challengeStatus = ACTIVE;
    }

    public void end() {
        this.challengeStatus = COMPLETED;
    }

    public ChallengeDomain toEntity() {
        return ChallengeDomain.builder()
                .id(id)
                .challengeStatus(challengeStatus)
                .completeRate(completeRate)
                .startDate(startDate)
                .endDate(endDate)
                .readingTimeGoal(readingTimeGoal)
                .build();
    }

    public void addChallengeUser(ChallengeUser challengeUser) {
        challengeUsers.add(challengeUser);
    }
}
