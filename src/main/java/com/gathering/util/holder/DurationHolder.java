package com.gathering.util.holder;

import java.time.LocalDateTime;

public interface DurationHolder {
    long calculateGoalDays(LocalDateTime startDate, LocalDateTime endDate);
}
