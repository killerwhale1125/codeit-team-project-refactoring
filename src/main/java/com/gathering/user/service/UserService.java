package com.gathering.user.service;

import com.gathering.common.base.exception.BaseException;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.GetAccessTokenDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;

public interface UserService {
    UserDto sginIn(SignInRequestDto requestDto);

    void signUp(SignUpRequestDto signUpRequestDto);
    boolean checkType(String param, boolean typeBol);
    UserDto selectUserInfo(String username);
}
