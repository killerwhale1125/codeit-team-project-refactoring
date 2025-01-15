package com.gathering.user.controller.port;

import com.gathering.user.domain.UserDto;
import com.gathering.user.domain.UserUpdate;
import com.gathering.user.domain.SignInRequestDto;
import com.gathering.user.domain.SignUpRequestDto;
import com.gathering.user_attendance_book.controller.response.UserAttendanceBookResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.YearMonth;
import java.util.List;

public interface UserService {
    UserDto sginIn(SignInRequestDto requestDto);

    void signUp(SignUpRequestDto signUpRequestDto);
    boolean checkType(String param, boolean typeBol);
    UserDto selectUserInfo(String username);

    UserDto editUser(UserUpdate userUpdate, MultipartFile file, String userName);

    List<UserAttendanceBookResponse> getBooksByCalendarDate(String username, YearMonth yearMonth);

    // 리프레시 토큰 redis에 저장
    void setRefreshTokenRedis(String key, String token);

    // 로그아웃 시 리프레시 토큰 삭제
    void deleteRefreshTokenRedis(String userRefreshTokenKey);

    // 리프레시 토큰을 활용한 accesstoken 재발급
    UserDto reissueToken(String userRefreshTokenKey);

}
