package com.gathering.common.base.response;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.gathering.common.base.response.BaseResponseStatus.SUCCESS;


@Getter
@AllArgsConstructor
public class BaseResponse<T> {
    private final boolean success;
    private final String message;
    private final String code;
    private T result;

    public BaseResponse() {
        this.success = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
    }

    /**
     * 요청 성공
     * @param result
     */
    public BaseResponse(T result) {
        this.success = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.result = result;
    }

    /**
     * 요청 실패
     * @param status
     */
    public BaseResponse(BaseResponseStatus status) {
        this.success = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
    }

}