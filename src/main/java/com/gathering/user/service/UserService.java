package com.gathering.user.service;

import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.EditUserRequestDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user.model.dto.response.UserAttendanceBookResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.YearMonth;
import java.util.List;

public interface UserService {
    UserDto sginIn(SignInRequestDto requestDto);

    void signUp(SignUpRequestDto signUpRequestDto);
    boolean checkType(String param, boolean typeBol);
    UserDto selectUserInfo(String username);

    UserDto editUser(EditUserRequestDto editUserRequestDto, MultipartFile file, String userName);

    List<UserAttendanceBookResponse> getBooksByCalendarDate(String username, YearMonth yearMonth);

    // 리프레시 토큰 redis에 저장
    void setRefreshTokenRedis(String key, String token);

    // 로그아웃 시 리프레시 토큰 삭제
    void deleteRefreshTokenRedis(String userRefreshTokenKey);

    // 리프레시 토큰을 활용한 accesstoken 재발급
    UserDto reissueToken(String userRefreshTokenKey);
}
