package com.gathering.util.date;

import java.time.LocalDate;
import java.util.Date;

public interface DateHolder {
    LocalDate now();

    Date createDate();

    Date createExpireDate(long now, long accessExpirationTime);
}
