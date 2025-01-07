package com.gathering.util.date;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * 기간 계산용 Util 클래스
 */
@Component
public class SystemDateCalculateUtils implements DateCalculateUtils {

    @Override
    public LocalDate getStartOfMonth(YearMonth yearMonth) {
        return yearMonth.atDay(1);
    }

    @Override
    public LocalDate getEndOfMonth(YearMonth yearMonth) {
        return yearMonth.atEndOfMonth();
    }
}
