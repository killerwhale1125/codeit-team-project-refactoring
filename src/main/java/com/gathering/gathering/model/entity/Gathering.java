package com.gathering.gathering.model.entity;

import com.gathering.book.model.entity.Book;
import com.gathering.challenge.model.entity.Challenge;
import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.jpa.BaseTimeEntity;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.validator.GatheringValidator;
import com.gathering.user.model.entitiy.User;
import com.gathering.util.holder.DateCalculateHolder;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gathering.common.base.response.BaseResponseStatus.*;
import static com.gathering.gathering.model.entity.GatheringStatus.*;

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
    private LocalDate endDateTime;
    private long goalDays;
    private int maxCapacity;
    private int minCapacity;
    private int currentCapacity;
    private long ownerId;
    private long viewCount;

    @Embedded
    private GatheringAddress gatheringAddress;

    @Enumerated(EnumType.STRING)
    private GatheringStatus gatheringStatus;

    @OneToMany(mappedBy = "gathering", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GatheringUser> gatheringUsers = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_Id")
    private Book book;

    public static Gathering createGathering(GatheringCreate gatheringCreate,
                                            Challenge challenge,
                                            Book book,
                                            GatheringUser gatheringUser,
                                            DateCalculateHolder dateCalculateHolder,
                                            GatheringValidator gatheringValidator) {
        Gathering gathering = new Gathering();
        gathering.name = gatheringCreate.getName();
        gathering.gatheringAddress = gatheringCreate.toAddress();
        gathering.content = gatheringCreate.getContent();
        gathering.endDateTime = gatheringCreate.getEndDate();
        gathering.goalDays = dateCalculateHolder.calculateGoalDays(gatheringCreate.getGatheringDate(), gatheringCreate.getEndDate());
        // 최소 최대 인원수 검증
        validateCapacity(gatheringCreate.getMinCapacity(), gatheringCreate.getMaxCapacity(), gatheringValidator);
        gathering.maxCapacity = gatheringCreate.getMaxCapacity();
        gathering.minCapacity = gatheringCreate.getMinCapacity();
        gathering.gatheringStatus = gatheringCreate.getGatheringStatus();
        gathering.book = book;
        gathering.currentCapacity = 1;
        gathering.ownerId = gatheringUser.getUser().getId();
        gathering.addChallenge(challenge);
        gathering.addGatheringUser(gatheringUser, gatheringValidator);
        return gathering;
    }

    // 모임 참여
    public static void join(Gathering gathering, User user,GatheringUser gatheringUser, GatheringValidator gatheringValidator) {
        // 이미 참여한 유저인지 검증
        gathering.isUserAlreadyJoined(user.getId());
        // 모임 상태가 참여할 수 있는 상태인지 검증
        gathering.validateGatheringStatus();
        gathering.addGatheringUser(gatheringUser, gatheringValidator);
        gathering.increaseCurrentCapacity();
        // 인원 FULL일 경우 모집완료 상태 변경
        gathering.checkIsFullAndUpdateStatus();
    }

    // 모임 떠나기
    public static void leave(Gathering gathering, User user) {
        List<GatheringUser> gatheringUsers = gathering.getGatheringUsers();
        GatheringUser gatheringUser = gatheringUsers.stream()
                .filter(gu -> gu.getUser().getId() == user.getId())
                .findFirst()
                .orElseThrow(() -> new BaseException(USER_NOT_IN_GATHERING));

        if(gatheringUser.getUser().getId() == gathering.getOwnerId())
            throw new BaseException(HOST_CANNOT_LEAVE_GATHERING);

        // 모임의 참여자 리스트에서 유저 제거
        gatheringUsers.remove(gatheringUser);
        // 현재 인원 수 감소
        gathering.decreaseCurrentCapacity();
        // 정원이 채워졌다면 모집 중으로 변경)
        gathering.checkIsFullAndUpdateStatus();
    }

    // 모임 유저 추가
    private void addGatheringUser(GatheringUser gatheringUser, GatheringValidator gatheringValidator) {
        gatheringValidator.validateCapacityLimit(this.currentCapacity, maxCapacity);
        gatheringUsers.add(gatheringUser);
        gatheringUser.addGathering(this);
    }

    // 모임에 챌린지 추가
    private void addChallenge(Challenge challenge) {
        this.challenge = challenge;
//        challenge.addGathering(this);
    }

    // 유저가 모임에 참여중인지 검증
    private void isUserAlreadyJoined(long userId) {
        if(gatheringUsers.stream()
                .anyMatch(gatheringUser -> gatheringUser.getUser().getId() == userId)) {
            throw new BaseException(ALREADY_JOINED);
        }
    }

    private static void validateCapacity(int minCapacity, int maxCapacity, GatheringValidator gatheringValidator) {
        if (gatheringValidator.validateMinCapacity(minCapacity)) {
            throw new BaseException(INVALID_MIN_CAPACITY);
        }
        if (gatheringValidator.validateMaxCapacity(maxCapacity)) {
            throw new BaseException(INVALID_MAX_CAPACITY);
        }
        if (gatheringValidator.validateCapacityRange(minCapacity, maxCapacity)) {
            throw new BaseException(INVALID_CAPACITY_RANGE);
        }
    }
    
    // 모임의 상태 검증
    private void validateGatheringStatus() {
        this.gatheringStatus.validate();
    }

    // 모임 인원 수 증가
    private void increaseCurrentCapacity() {
        this.currentCapacity++;
    }

    // 모임 인원 수 감소
    private void decreaseCurrentCapacity() {
        this.currentCapacity--;
    }

    public void increaseViewCount(long viewCount) {
        this.viewCount += viewCount;
    }

    // 모임 상태 변화
    private void checkIsFullAndUpdateStatus() {
        if (this.currentCapacity >= this.maxCapacity) {
            this.gatheringStatus = FULL;
        }

        if (this.currentCapacity < this.maxCapacity && this.gatheringStatus == FULL) {
            this.gatheringStatus = RECRUITING;
        }
    }
}
