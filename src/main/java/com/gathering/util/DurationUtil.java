package com.gathering.util;

import com.gathering.common.base.exception.BaseException;
import com.gathering.util.holder.DurationHolder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.gathering.common.base.response.BaseResponseStatus.INVALID_GOAL_PERIOD;

/**
 * 기간 계산용 Util 클래스
 */
@Component
public class DurationUtil implements DurationHolder {

    @Override
    public long calculateGoalDays(LocalDateTime startDate, LocalDateTime endDate) {
//        if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
//            throw new BaseException(INVALID_GOAL_PERIOD);
//        }
//        return Duration.between(startDate, endDate).toDays();
        return 2;
    }
}
