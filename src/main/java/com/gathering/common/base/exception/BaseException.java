package com.gathering.common.base.exception;

import com.gathering.common.base.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private BaseResponseStatus status;
}
