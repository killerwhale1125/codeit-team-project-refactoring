package com.gathering.user.service;

import com.gathering.common.base.exception.BaseException;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.GetAccessTokenDto;
import com.gathering.user.model.dto.request.SignInRequestDto;

public interface UserService {
    String getAccessToken(GetAccessTokenDto getAccessTokenDto) throws BaseException;

    UserDto sginIn(SignInRequestDto requestDto);
}
