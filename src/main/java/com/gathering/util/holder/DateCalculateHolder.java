package com.gathering.util.holder;

import java.time.LocalDate;

public interface DateCalculateHolder {
    long calculateGoalDays(LocalDate startDate, LocalDate endDate);
}
