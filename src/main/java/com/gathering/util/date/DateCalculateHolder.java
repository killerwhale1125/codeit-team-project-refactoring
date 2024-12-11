package com.gathering.util.date;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateCalculateHolder {
    long calculateGoalDays(LocalDate startDate, LocalDate endDate);

    long calculateSecondsUntilStart(LocalDate startDate, LocalDateTime now);
}
