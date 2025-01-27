package com.gathering.gathering.domain;

public enum GatheringWeek {
    ONE_WEEK(7),
    TWO_WEEKS(14),
    THREE_WEEKS(21),
    FOUR_WEEKS(28),
    FIVE_WEEKS(35),
    SIX_WEEKS(42),
    SEVEN_WEEKS(49),
    EIGHT_WEEKS(56);

    private final int week;

    // 생성자
    GatheringWeek(int week) {
        this.week = week;
    }

    // getter
    public int getWeek() {
        return week;
    }

}
