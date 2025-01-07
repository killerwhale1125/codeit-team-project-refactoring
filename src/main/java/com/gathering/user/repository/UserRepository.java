package com.gathering.user.repository;

import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.EditUserRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.model.entitiy.UserAttendance;

import java.time.LocalDate;
import java.util.List;
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
    UserDto editUser(EditUserRequestDto editUserRequestDto, String fileName, long userId);

    UserDomain save(UserDomain user);

    // 이메일로 사용자 찾기
    UserDto selectUserByEmail(String email);

    Set<Long> findWishGatheringIdsByUserName(String username);

    boolean existsByUserName(String username);

    List<UserAttendance> getUserAttendancesByUserIdAndDate(Long id, LocalDate startDate, LocalDate endDate);

}
