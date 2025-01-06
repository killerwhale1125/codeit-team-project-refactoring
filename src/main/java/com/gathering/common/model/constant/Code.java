package com.gathering.common.model.constant;

import lombok.Getter;

@Getter
public enum Code {

    PROFILE("PF", "FILE");

    private final String code;
    private final String codeGrp;

    Code(String code, String codeGrp) {
        this.code = code;
        this.codeGrp = codeGrp;
    }
}
