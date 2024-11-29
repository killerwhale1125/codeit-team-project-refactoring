package com.gathering.user.repository.temp;

import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserRepository {
    
    // 사용자 정보 조회
    UserDto selectUser(SignInRequestDto requestDto);

    // 출석 체크
    int insertAttendance(long usersId);

    // 회원 가입
    int signUp(SignUpRequestDto signUpRequestDto, MultipartFile file);
}
