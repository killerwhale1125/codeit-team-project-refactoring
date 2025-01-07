package com.gathering.util.string;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SystemUUIDUtils implements UUIDUtils {

    @Override
    public String getRandom() {
        return UUID.randomUUID().toString();
    }
}
