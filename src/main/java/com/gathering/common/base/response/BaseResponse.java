package com.gathering.common.base.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.gathering.common.base.response.BaseResponseStatus.SUCCESS;


@Getter
@AllArgsConstructor
public class BaseResponse<T> {
    private final int status;
    private final String message;
    private final String code;
    private T result;

    public BaseResponse() {
        this.status = SUCCESS.getHttpStatus().value();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
    }

    /**
     * 요청 성공
     * @param result
     */
    public BaseResponse(T result) {
        this.status = SUCCESS.getHttpStatus().value();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.result = result;
    }

    /**
     * 요청 실패
     * @param status
     */
    public BaseResponse(BaseResponseStatus status) {
        this.status = status.getHttpStatus().value();
        this.message = status.getMessage();
        this.code = status.getCode();
    }

}