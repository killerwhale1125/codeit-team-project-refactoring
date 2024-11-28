package com.gathering.user.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.user.model.dto.request.GetAccessTokenDto;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.response.UserResponseDto;
import com.gathering.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "사용자 API", description = "사용자 응답 관련 api")
@ApiResponse(responseCode = "200", description = "success")
@ApiResponse(responseCode = "500", description = "failed")
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

        UserDto userDto = userService.sginIn(requestDto);
        return new BaseResponse<>(userDto);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @Operation(summary = "회원가입", description = "signup")
    public String test(
        @RequestBody UserDto userDto
    ) {


        return "signup";
    }
}
