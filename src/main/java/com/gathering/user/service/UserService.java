package com.gathering.user.service;

import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.EditUserRequestDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDto sginIn(SignInRequestDto requestDto);

    void signUp(SignUpRequestDto signUpRequestDto);
    boolean checkType(String param, boolean typeBol);
    UserDto selectUserInfo(String username);

    UserDto editUser(EditUserRequestDto editUserRequestDto, MultipartFile file, String userName);
}
