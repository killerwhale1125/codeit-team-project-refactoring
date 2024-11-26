package com.gathering.common.base.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    SUCCESS("SU", "Success");

    private final String code;
    private final String message;

    private BaseResponseStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
