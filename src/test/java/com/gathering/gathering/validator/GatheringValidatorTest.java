package com.gathering.gathering.validator;

import com.gathering.common.base.exception.BaseException;
import com.gathering.gathering.util.GatheringValidator;
import com.gathering.gatheringuser.domain.GatheringUserDomain;
import com.gathering.mock.test.TestGatheringValidator;
import com.gathering.user.model.domain.UserDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GatheringValidatorTest {

    private GatheringValidator testGatheringValidator;

    @BeforeEach
    void setUp() {
        this.testGatheringValidator = new TestGatheringValidator();
    }

    @Test
    @DisplayName("모임의 인원이 꽉차지 않았다면 사용자는 모임에 참여할 수 있다.")
    void validateCapacityLimitSuccess() {
        /* given */
        int currentCapacity = 8;
        int maxCapacity = 10;
        /* when */
        testGatheringValidator.validateCapacityLimit(currentCapacity, maxCapacity);
        /* then */
        // Success
    }

    @Test
    @DisplayName("모임에 인원이 꽉 찼다면 사용자는 모임에 참여할 수 없다.")
    void validateCapacityLimitFail() {
        /* given */
        int currentCapacity = 10;
        int maxCapacity = 10;
        /* then when */
        assertThatThrownBy(() -> testGatheringValidator.validateCapacityLimit(currentCapacity, maxCapacity))
                .isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("해당 사용자는 모임장이다.")
    void validateOwnerSuccess() {
        /* given */
        String owner = "범고래1";
        String user = "범고래1";

        /* when */
        testGatheringValidator.validateOwner(owner, user);

        /* then */
        // Success
    }

    @Test
    @DisplayName("해당 사용자는 모임장이 아니다.")
    void validateOwnerFail() {
        /* given */
        String owner = "범고래1";
        String user = "범고래2";

        /* when then */
        assertThatThrownBy(() -> testGatheringValidator.validateOwner(owner, user))
                .isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("최소 인원이 0보다 크다.")
    void validateMinCapacityFalse() {
        /* given */
        int minCapacity = 5;

        /* when */
        boolean result = testGatheringValidator.validateMinCapacity(minCapacity);

        /* then */
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("최소 인원이 0보다 작다.")
    void validateMinCapacityTrue() {
        /* given */
        int minCapacity = 0;

        /* when */
        boolean result = testGatheringValidator.validateMinCapacity(minCapacity);

        /* then */
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("최대 인원이 0보다 크다.")
    void validateMaxCapacityFalse() {
        /* given */
        int minCapacity = 5;

        /* when */
        boolean result = testGatheringValidator.validateMaxCapacity(minCapacity);

        /* then */
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("최대 인원이 0보다 작다.")
    void validateMaxCapacityTrue() {
        /* given */
        int minCapacity = 0;

        /* when */
        boolean result = testGatheringValidator.validateMaxCapacity(minCapacity);

        /* then */
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("최소 인원이 최대 인원보다 작다.")
    void validateCapacityRangeFalse() {
        /* given */
        int minCapacity = 5;
        int maxCapacity = 10;

        /* when */
        boolean result = testGatheringValidator.validateCapacityRange(minCapacity, maxCapacity);

        /* then */
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("최소 인원이 최대 인원보다 작다.")
    void validateCapacityRangeTrue() {
        /* given */
        int minCapacity = 10;
        int maxCapacity = 5;

        /* when */
        boolean result = testGatheringValidator.validateCapacityRange(minCapacity, maxCapacity);

        /* then */
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("모임 시작일은 오늘 날짜와 같지 않고 더 나중이다.")
    void validateJoinDateSuccess() {
        /* given */
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.plusDays(10);

        /* when */
        testGatheringValidator.validateJoinDate(startDate, now);
        
        /* then */
        // Success
    }

    @Test
    @DisplayName("모임 시작일은 오늘날짜와 같거나 전일 경우 예외가 발생한다.")
    void validateJoinDateFail() {
        /* given */
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(10);

        /* when then */
        assertThatThrownBy(() -> testGatheringValidator.validateJoinDate(startDate, now))
                .isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("사용자가 모임에 참여하지 않은 경우는 참여가 가능하다.")
    void validateAlreadyJoinedUserSuccess() {
        /* given */

        UserDomain user1 = UserDomain.builder().id(1L).build();
        UserDomain user2 = UserDomain.builder().id(2L).build();
        GatheringUserDomain gatheringUser = GatheringUserDomain.builder()
                .user(user2)
                .build();

        /* when */
        testGatheringValidator.validateAlreadyJoinedUser(List.of(gatheringUser), user1);

        /* then */
        // Success
    }

    @Test
    @DisplayName("사용자가 모임에 참여하고 있는 경우 참여가 불가능하다.")
    void validateAlreadyJoinedUserFail() {
        /* given */

        UserDomain user1 = UserDomain.builder().id(1L).build();
        GatheringUserDomain gatheringUser = GatheringUserDomain.builder()
                .user(user1)
                .build();

        /* when then */
        assertThatThrownBy(() -> testGatheringValidator.validateAlreadyJoinedUser(List.of(gatheringUser), user1))
                .isInstanceOf(BaseException.class);

    }
}
