package com.gathering.user.repository;

import com.gathering.common.base.exception.BaseException;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user.model.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.gathering.common.base.response.BaseResponseStatus.NOT_EXISTED_USER;

@Repository("userRepository")
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{

    private final UserJpaRepository userJpaRepository;
    private final UserAttendanceJpaRepository userAttendanceJpaRepository;

    @Override
    public UserDto selectUser(SignInRequestDto requestDto) {

        User user = userJpaRepository.findByUserName(requestDto.userName()).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));

        return UserDto.fromEntity(user);
    }

    @Override
    public int insertAttendance(long usersId) { return userAttendanceJpaRepository.insertAttendance(usersId); }

    @Override
    public User findByUsername(String username) {
        return userJpaRepository.findByUserName(username).orElseThrow(() -> new BaseException(NOT_EXISTED_USER));
    }

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        User user = User.createUser(signUpRequestDto);
        userJpaRepository.save(user);
    }

    @Override
    public boolean checkType(String param, boolean typeBol) {
        Optional<User> user = typeBol ? userJpaRepository.findByEmail(param) : userJpaRepository.findByUserName(param);
        if (user.isEmpty()) {
            return true;
        }
        return false;
    }

}
