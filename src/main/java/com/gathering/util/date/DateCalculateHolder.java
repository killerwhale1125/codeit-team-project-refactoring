package com.gathering.util.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

public interface DateCalculateHolder {
    long calculateSecondsUntilStart(LocalDate startDate, LocalDateTime now);

    LocalDate getStartOfMonth(YearMonth yearMonth);

    LocalDate getEndOfMonth(YearMonth yearMonth);
}
