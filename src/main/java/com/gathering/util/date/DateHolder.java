package com.gathering.util.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface DateHolder {
    LocalDate localDateNow();

    LocalDateTime localDateTimeNow();

    Date createDate();

    Date createExpireDate(long now, long accessExpirationTime);
}
