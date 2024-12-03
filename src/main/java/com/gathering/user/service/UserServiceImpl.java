package com.gathering.user.service;

import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user.repository.UserRepository;
import com.gathering.user.util.AdminToken;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${path.user.profile}")
    private String serverPath;

    @Override
    public UserDto sginIn(SignInRequestDto requestDto) {

        UserDto userDto = userRepository.selectUser(requestDto.userName());

        if (userDto != null || userDto.getPassword().equals(passwordEncoder.encode(requestDto.password()))) {
            userRepository.insertAttendance(userDto.getUsersId());
            userDto.setPassword(null);
            return userDto;
        } else {
            return null;
        }

    }

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        // 비밀번호 암호화
        signUpRequestDto.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        userRepository.signUp(signUpRequestDto);
    }

    @Override
    public boolean checkType(String param, boolean typeBol) {
        return userRepository.checkType(param, typeBol);
    }

    @Override
    public UserDto selectUserInfo(String username) {
        return userRepository.selectUser(username);
    }

}
