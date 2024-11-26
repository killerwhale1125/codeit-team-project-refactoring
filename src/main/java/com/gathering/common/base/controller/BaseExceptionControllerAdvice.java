package com.gathering.common.base.controller;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BaseExceptionControllerAdvice {
    @ExceptionHandler
    public BaseResponse<BaseResponseStatus> baseException(BaseException e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder logMessage = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            logMessage.append(stackTrace[i]).append(", ");
        }
        log.error("BaseException Error : " + "Error status : " + e.getStatus().toString() + ", Path = " + logMessage);
        return new BaseResponse<>(e.getStatus());
    }
}
