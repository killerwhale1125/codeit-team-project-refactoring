package com.gathering.gathering.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.book.domain.BookDomain;
import com.gathering.challenge.domain.ChallengeDomain;
import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.util.GatheringValidator;
import com.gathering.gathering_review.domain.GatheringReviewDomain;
import com.gathering.gatheringuser.domain.GatheringUserDomain;
import com.gathering.image.domain.ImageDomain;
import com.gathering.user.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gathering.common.base.response.BaseResponseStatus.*;
import static com.gathering.gathering.domain.GatheringStatus.*;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GatheringDomain {
    private Long id;
    private String name;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    private int maxCapacity;
    private int minCapacity;
    private int currentCapacity;
    private String owner;
    private long viewCount;
    private GatheringStatus gatheringStatus;
    private List<GatheringUserDomain> gatheringUsers;
    private List<GatheringReviewDomain> gatheringReviews;
    private ChallengeDomain challenge;
    private BookDomain book;
    private GatheringWeek gatheringWeek;
    private ImageDomain image;

    public static GatheringDomain create(GatheringCreate gatheringCreate,
                                         ChallengeDomain challenge,
                                         BookDomain book,
                                         List<ImageDomain> images,
                                         UserDomain user,
                                         GatheringValidator gatheringValidator) {

        validateCapacity(gatheringCreate.getMinCapacity(), gatheringCreate.getMaxCapacity(), gatheringValidator);
        validateDate(gatheringCreate.getStartDate(), gatheringCreate.getEndDate());
        return GatheringDomain.builder()
                .name(gatheringCreate.getName())
                .content(gatheringCreate.getContent())
                .startDate(gatheringCreate.getStartDate())
                .endDate(gatheringCreate.getEndDate())
                .minCapacity(checkUnlimitedMinCapacity(gatheringCreate.getMinCapacity(),
                        gatheringCreate.getMaxCapacity()))
                .maxCapacity(gatheringCreate.getMaxCapacity())
                .currentCapacity(1)
                .owner(user.getUserName())
                .viewCount(0)
                .gatheringStatus(gatheringCreate.getGatheringStatus())
                .gatheringUsers(new ArrayList<>())
                .challenge(challenge)
                .book(book)
                .gatheringWeek(gatheringCreate.getGatheringWeek())
                .image(images.get(0))
                .build();
    }

    public static void join(GatheringDomain gathering, UserDomain user, GatheringValidator gatheringValidator) {
        // 이미 참여한 유저인지 검증
        gatheringValidator.validateAlreadyJoinedUser(gathering.getGatheringUsers(), user);
        // 모임(챌린지) 시작 전에만 참여할 수 있다.
        gatheringValidator.validateJoinDate(gathering.getStartDate(), LocalDate.now());
        // 모임에 참여 가능한지 검사
        gatheringValidator.validateCapacityLimit(gathering.getCurrentCapacity(), gathering.getMaxCapacity());
        // 모임 상태가 참여할 수 있는 상태인지 검증
        gathering.getGatheringStatus().validate();

        /* 상태 변화 */
        // 한명이 추가로 참여했기 때문에 모임의 현재 인원을 증가
        gathering.increaseCurrentCapacity();
        // 현재 참여 인원 FULL일 경우 모집 완료 상태로 변경한다.
        gathering.checkIsFullAndUpdateStatus();
    }

    public static GatheringUserDomain leave(GatheringDomain gathering, UserDomain user) {
        List<GatheringUserDomain> gatheringUsers = gathering.getGatheringUsers();
        GatheringUserDomain gatheringUser = gatheringUsers.stream()
                .filter(gu -> gu.getUser().getId() == user.getId())
                .findFirst()
                .orElseThrow(() -> new BaseException(USER_NOT_IN_GATHERING));

        if(gatheringUser.getUser().getUserName() == gathering.getOwner())
            throw new BaseException(HOST_CANNOT_LEAVE_GATHERING);

        // 모임의 참여자 리스트에서 유저 제거
        gatheringUsers.remove(gatheringUser);
        // 현재 인원 수 감소
        gathering.decreaseCurrentCapacity();
        // 정원이 채워졌다면 모집 중으로 변경)
        gathering.checkIsFullAndUpdateStatus();

        return gatheringUser;
    }

    private static void validateDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate) || startDate.isBefore(LocalDate.now()) || startDate.isEqual(endDate)) {
            throw new BaseException(INVALID_DATE_RANGE);
        }
    }

    private static int checkUnlimitedMinCapacity(int minCapacity, int maxCapacity) {
        return minCapacity == Integer.MAX_VALUE && maxCapacity == Integer.MAX_VALUE ? 5 : minCapacity;
    }

    private static void validateCapacity(int minCapacity, int maxCapacity, GatheringValidator gatheringValidator) {
        // 제한 없음일 경우는 유효성 체크를 하지 않는다.
        if(minCapacity == Integer.MAX_VALUE && maxCapacity == Integer.MAX_VALUE) return;

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

    // 모임을 참여했거나 떠날 때 참여자 수를 검사하여 상태를 변경한다.
    private void checkIsFullAndUpdateStatus() {
        if (this.currentCapacity >= this.maxCapacity) {
            this.gatheringStatus = FULL;
        }

        if (this.currentCapacity < this.maxCapacity && this.gatheringStatus == FULL) {
            this.gatheringStatus = RECRUITING;
        }
    }

    // 현재 인원을 증가시킨다.
    private void increaseCurrentCapacity() {
        this.currentCapacity++;
    }

    private void decreaseCurrentCapacity() {
        this.currentCapacity--;
    }

    public static GatheringDomain end(GatheringDomain gathering) {
        gathering.gatheringStatus = COMPLETED;
        return gathering;
    }
}
