package com.gathering.util;

import com.gathering.util.holder.DurationHolder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 기간 계산용 Util 클래스
 */
@Component
public class DurationUtil implements DurationHolder {

    @Override
    public long calculateGoalDays(LocalDateTime startDate, LocalDateTime endDate) {
        return Duration.between(startDate, endDate).toDays();
    }
}
