package com.gathering.gathering.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gathering.book.model.domain.BookDomain;
import com.gathering.challenge.model.domain.ChallengeDomain;
import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.model.dto.GatheringCreate;
import com.gathering.gathering.model.entity.GatheringStatus;
import com.gathering.gathering.model.entity.GatheringWeek;
import com.gathering.gathering.validator.GatheringValidator;
import com.gathering.gatheringuser.model.domain.GatheringUserDomain;
import com.gathering.image.model.domain.ImageDomain;
import com.gathering.user.model.domain.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.gathering.common.base.response.BaseResponseStatus.*;

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
}
