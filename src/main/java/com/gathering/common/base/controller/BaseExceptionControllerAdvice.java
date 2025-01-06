package com.gathering.common.base.controller;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BaseExceptionControllerAdvice {
    @ExceptionHandler(BaseException.class)
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<BaseResponse<BaseResponseStatus>> handleBaseException(BaseException e) {
        // 예외 발생 로그 출력
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder logMessage = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            logMessage.append(stackTrace[i]).append(", ");
        }
        log.error("BaseException Error : " + "Error status : " + e.getStatus().toString() + ", Path = " + logMessage);

        BaseResponse<BaseResponseStatus> response = new BaseResponse<>(e.getStatus());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500 상태 코드
    }
    
    // dto에 대한 유효성 검사 에러 응답

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<BaseResponse<BaseResponseStatus>> handleValidationExceptions(MethodArgumentNotValidException e) {
        BaseResponse<BaseResponseStatus> response = new BaseResponse<>(BaseResponseStatus.INVALID_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 상태 코드
    }

    // request param에 대한 유효성 검사 에러 응답
    @ExceptionHandler(ConstraintViolationException.class)
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<BaseResponse<BaseResponseStatus>> handleConstraintViolationException(ConstraintViolationException e) {
        BaseResponse<BaseResponseStatus> response = new BaseResponse<>(BaseResponseStatus.INVALID_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 상태 코드
    }
}
