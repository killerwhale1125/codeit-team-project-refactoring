package com.gathering.user.model.constant;

import lombok.Getter;

@Getter
public enum SingUpType {

    EMAIL("email"),
    ID("userName");

    private final String value;

    SingUpType(String value) {
        this.value = value;
    }

}
