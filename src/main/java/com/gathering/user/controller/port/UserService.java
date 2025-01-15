package com.gathering.user.controller.port;

import com.gathering.user.domain.*;
import com.gathering.user_attendance_book.controller.response.UserAttendanceBookResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.YearMonth;
import java.util.List;

public interface UserService {
    UserResponse login(UserLogin requestDto);

    void signUp(UserSignUp userSignUp);

    UserResponse findByUsername(String username);

    UserResponse update(UserUpdate userUpdate, MultipartFile file, String userName);

    List<UserAttendanceBookResponse> getBooksByCalendarDate(String username, YearMonth yearMonth);

    // 리프레시 토큰을 활용한 accesstoken 재발급
    UserResponse reissueToken(String userRefreshTokenKey);

    void verifyUsernameOrEmail(String param, SingUpType type);

    void verifyPassword(PasswordCheck passwordCheck, String username);
}
