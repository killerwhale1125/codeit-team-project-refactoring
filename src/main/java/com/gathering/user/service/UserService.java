package com.gathering.user.service;

import com.gathering.common.base.exception.BaseException;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.GetAccessTokenDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    String getAccessToken(GetAccessTokenDto getAccessTokenDto) throws BaseException;

    UserDto sginIn(SignInRequestDto requestDto);

    int signUp(SignUpRequestDto signUpRequestDto, MultipartFile file);
}
