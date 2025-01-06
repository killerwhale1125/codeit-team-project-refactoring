package com.gathering.util.date;

import com.gathering.common.base.exception.BaseException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

import static com.gathering.common.base.response.BaseResponseStatus.INVALID_CHALLENGE_START_BETWEEN_TIME;
import static com.gathering.common.base.response.BaseResponseStatus.INVALID_GOAL_PERIOD;

/**
 * 기간 계산용 Util 클래스
 */
@Component
public class DateCalculateUtil implements DateCalculateHolder {

    @Override
    public long calculateSecondsUntilStart(LocalDate startDate, LocalDateTime now) {
        long between = ChronoUnit.SECONDS.between(now, startDate.atStartOfDay());
        if(between < 0) {
            throw new BaseException(INVALID_CHALLENGE_START_BETWEEN_TIME);
        }
        return between;
    }

    @Override
    public LocalDate getStartOfMonth(YearMonth yearMonth) {
        return yearMonth.atDay(1);
    }

    @Override
    public LocalDate getEndOfMonth(YearMonth yearMonth) {
        return yearMonth.atEndOfMonth();
    }
}
