package com.gathering.util.date;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class SystemDate implements DateHolder {

    public LocalDate now() {
        return LocalDate.now();
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
