package com.gathering.gathering.model.entity;

import org.joda.time.Weeks;

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

//    // 주기 계산 (옵션)
//    public static Weeks fromWeeks(int weekCount) {
//        for (Weeks weeks : Weeks.values()) {
//            if (weeks.getWeekCount() == weekCount) {
//                return weeks;
//            }
//        }
//        throw new IllegalArgumentException("Invalid week count: " + weekCount);
//    }
}
