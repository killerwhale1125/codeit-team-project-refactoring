package com.gathering.review.model.constant;

import lombok.Getter;

@Getter
public enum ReviewType {

    GATHERING("GATHERING"),
    BOOK("BOOK");

    private final String value;

    ReviewType(String value) {
        this.value = value;
    }
}
