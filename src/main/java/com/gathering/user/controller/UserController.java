package com.gathering.user.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.user.model.constant.SingUpType;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.GetAccessTokenDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user.service.UserService;
import com.gathering.user.util.Validator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "사용자 API", description = "사용자 응답 관련 api")
@ApiResponse(responseCode = "200", description = "success")
@Validated  // 파라미터의 유효성 검사를 활성화
public class UserController {


    @Resource(name = "userService")
    private final UserService userService;

    @RequestMapping(value = "/getAccessToken", method = RequestMethod.POST)
    @Operation(summary = "토큰 발급(임시)", description = "개발을 위한 토큰 발급 api 실제 프론트 구현시 프론트에서 직접 발급 필요")
    public BaseResponse<String> getAccessToken(
            @RequestBody GetAccessTokenDto getAccessTokenDto
            ){
        String accessToken = userService.getAccessToken(getAccessTokenDto);
        return new BaseResponse<>(accessToken);
    }

    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    @Operation(summary = "로그인", description = "사용자 로그인")
    public BaseResponse<UserDto> signIn(
            @Valid @RequestBody SignInRequestDto requestDto) {
        // 토큰 발급
        GetAccessTokenDto getAccessTokenDto= new GetAccessTokenDto();
        getAccessTokenDto.setUserId(requestDto.userName());
        getAccessTokenDto.setPassword(requestDto.password());
        String accessToken = userService.getAccessToken(getAccessTokenDto);

        UserDto userDto = userService.sginIn(requestDto);
        userDto.setToken(accessToken);
        return new BaseResponse<>(userDto);
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    @Operation(summary = "회원가입", description = "signUp")
    public BaseResponse<Void> signUp(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto  // JSON 데이터
    ) {
        userService.signUp(signUpRequestDto);
        return new BaseResponse<>();
    }

    @RequestMapping(value = "/check/{type}", method = RequestMethod.GET)
    @Operation(summary = "email/userName 체크", description = "email/userName 체크 " +
            "type이 email일 경우 이메일 체크" +
            "type이 userName일 경우 아이디 체크 결과 반환")
    public BaseResponse<Void> checkType(
            @RequestParam String param,
            @PathVariable String type
    ) {

        boolean typeBol = type.equals(SingUpType.EMAIL.getValue());

        if (param == null || param.isBlank()) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_REQUEST);
        }

        // 이메일일 경우 이메일 유효성 검사
        if (typeBol && !Validator.isValidEmail(param)) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_REQUEST);
        }
        boolean result = userService.checkType(param, typeBol);

        if(result) {
            return new BaseResponse<>();
        }
        return new BaseResponse<>(typeBol ? BaseResponseStatus.DUPLICATE_EMAIL : BaseResponseStatus.DUPLICATE_USERNAME);
    }
}
