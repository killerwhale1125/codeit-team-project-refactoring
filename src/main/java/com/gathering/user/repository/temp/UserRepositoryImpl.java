package com.gathering.user.repository.temp;

import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user.model.entitiy.User;
import com.gathering.user.repository.UserAttendanceJpaRepository;
import com.gathering.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository("userRepository")
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserAttendanceJpaRepository userAttendanceJpaRepository;

    @Override
    public UserDto selectUser(SignInRequestDto requestDto) {

        User user = userJpaRepository.findByUserName(requestDto.userName());

        return UserDto.fromEntity(user);
    }

    @Override
    public int insertAttendance(long usersId) { return userAttendanceJpaRepository.insertAttendance(usersId); }

    @Override
    public int signUp(SignUpRequestDto signUpRequestDto, MultipartFile file) {
        return 0;
    }
}
