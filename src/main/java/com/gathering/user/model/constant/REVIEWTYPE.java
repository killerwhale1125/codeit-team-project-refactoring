package com.gathering.user.model.constant;

import lombok.Getter;

@Getter
public enum REVIEWTYPE {

    GATHERING("GATHERING"),
    BOOK("BOOK");

    private final String value;

    REVIEWTYPE(String value) {
        this.value = value;
    }
}
