package com.gathering.util.date;

import java.time.LocalDate;
import java.time.YearMonth;

public interface DateCalculateUtils {

    LocalDate getStartOfMonth(YearMonth yearMonth);

    LocalDate getEndOfMonth(YearMonth yearMonth);
}
