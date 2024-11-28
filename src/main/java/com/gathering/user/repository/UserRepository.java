package com.gathering.user.repository;

import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.SignInRequestDto;

public interface UserRepository {
    
    // 사용자 정보 조회
    UserDto selectUser(SignInRequestDto requestDto);

    // 출석 체크
    int insertAttendance(long usersId);
}
