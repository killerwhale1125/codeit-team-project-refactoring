package com.gathering.user.repository;

import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.EditUserRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user.model.entitiy.User;

public interface UserRepository {
    
    // 사용자 정보 조회
    UserDto selectUser(String userName);

    // 출석 체크
    int insertAttendance(long usersId);

    User findByUsername(String username);

    // 회원 가입
    void signUp(SignUpRequestDto signUpRequestDto);
    // 이메일/아이디 체크
    boolean checkType(String param, boolean typeBol);

    // 사용자 수정
    UserDto editUser(EditUserRequestDto editUserRequestDto, String fileName, long userId);

    void save(User user);
}
