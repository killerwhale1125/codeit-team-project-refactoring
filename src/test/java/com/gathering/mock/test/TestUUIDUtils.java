package com.gathering.mock.test;

import com.gathering.util.string.UUIDUtils;

public class TestUUIDUtils implements UUIDUtils {

    private final String uuid;

    public TestUUIDUtils(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getRandom() {
        return uuid;
    }
}
