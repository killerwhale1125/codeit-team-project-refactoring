package com.gathering.user.service.port;

import com.gathering.user.domain.UserDomain;
import com.gathering.user.domain.UserDto;
import com.gathering.user.domain.UserUpdate;
import com.gathering.user.domain.SignUpRequestDto;

import java.util.Set;

public interface UserRepository {
    
    // 사용자 정보 조회
    UserDto selectUser(String userName);

    // 출석 체크
    int insertAttendance(long usersId);

    UserDomain findByUsername(String username);

    // 회원 가입
    void signUp(SignUpRequestDto signUpRequestDto);
    // 이메일/아이디 체크
    boolean checkType(String param, boolean typeBol);

    // 사용자 수정
    UserDto editUser(UserUpdate userUpdate, String fileName, long userId);

    UserDomain save(UserDomain user);

    // 이메일로 사용자 찾기
    UserDto selectUserByEmail(String email);

    Set<Long> findWishGatheringIdsByUserName(String username);

}
