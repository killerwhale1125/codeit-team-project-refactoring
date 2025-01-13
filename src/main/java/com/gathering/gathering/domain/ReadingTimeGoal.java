package com.gathering.gathering.domain;

public enum ReadingTimeGoal {
    ONE_HOUR(60), // 1시간
    OVER_ONE_HOUR(Integer.MAX_VALUE), // 1시간 이상 (최대값으로 처리)
    TEN_MINUTES(10), // 10분
    THIRTY_MINUTES(30); // 30분

    private final int minutes;

    ReadingTimeGoal(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return minutes;
    }
}
