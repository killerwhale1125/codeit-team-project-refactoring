package com.gathering.util.date;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class SystemDate implements DateHolder {

    public LocalDate localDateNow() {
        return LocalDate.now();
    }

    public LocalDateTime localDateTimeNow() {
        return LocalDateTime.now();
    }

    @Override
    public Date createDate() {
        return new Date();
    }

    @Override
    public Date createExpireDate(long now, long accessExpirationTime) {
        return new Date(now + accessExpirationTime);
    }
}
