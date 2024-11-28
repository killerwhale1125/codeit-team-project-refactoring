package com.gathering.gathering.model.entity;

import com.gathering.challenge.model.entity.Challenge;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.dto.GatheringCreate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gathering extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gathering_id")
    private Long id;

    private String name;
    private String content;
    private LocalDateTime endDateTime;
    private long goalDays;
    private int maxCapacity;
    private int minCapacity;

    @Enumerated(EnumType.STRING)
    private GatheringType gatheringType;

    @Embedded
    private GatheringAddress gatheringAddress;

    @Enumerated
    private GatheringStatus gatheringStatus;

    @OneToMany(mappedBy = "gathering", cascade = CascadeType.ALL)
    private List<GatheringUser> gatheringUser = new ArrayList<>();

    /**
     * 모임이 챌린지를 주체(소유)한다
     * 주체자 -> Gathering (주인)
     * 종속자 -> Challenge
     * 즉 Challenge 의 상태를 변경하려면 Gathering의 challenge 필드로 변경이 가능하다
     * ex )
     * gathering.setChallenge() -> O
     * challenge.setGathering() -> X
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    public static Gathering create(GatheringCreate gatheringCreate,Challenge challenge, GatheringUser gatheringUser, long goalDays) {
        Gathering gathering = new Gathering();
        gathering.name = gatheringCreate.getName();
        gathering.gatheringType = gatheringCreate.getGatheringType();
        gathering.gatheringAddress = gatheringCreate.toAddress();
        gathering.content = gatheringCreate.getContent();
        gathering.endDateTime = gatheringCreate.getEndDate();
        gathering.goalDays = goalDays;
        gathering.maxCapacity = gathering.getMaxCapacity();
        gathering.minCapacity = gathering.getMinCapacity();
        gathering.gatheringStatus = GatheringStatus.RECRUITING;
        gathering.challenge = challenge;
        gathering.getGatheringUser().add(gatheringUser);
        return gathering;
    }
}
