package com.gathering.user.service;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.common.model.constant.Code;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.EditUserRequestDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user.repository.UserRepository;
import com.gathering.util.file.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.gathering.common.base.response.BaseResponseStatus.BOOK_OR_CATEGORY_NOT_FOUND;

@RequiredArgsConstructor
@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUtil fileUtil;

    @Value("${path.user.profile}")
    private String serverPath;



    @Override
    public UserDto sginIn(SignInRequestDto requestDto) {

        UserDto userDto = userRepository.selectUser(requestDto.userName());

        if (userDto == null) {
            return null;
        } else if(userDto.getPassword().equals(passwordEncoder.encode(requestDto.password()))) {
            userRepository.insertAttendance(userDto.getUsersId());
            userDto.setPassword(null);
            return userDto;
        } else {
            throw new BaseException(BaseResponseStatus.SIGN_IN_FAIL);
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

    @Override
    public UserDto editUser(EditUserRequestDto editUserRequestDto, MultipartFile file, String userName) {

        String fileName = "";
        UserDto userDto = userRepository.selectUser(userName);
        if( file != null) {
            String profile = userDto.getProfile();

            // 기존 프로필 파일 삭제
            if(profile != null && !profile.isEmpty()) {
                fileUtil.DeleteFile(profile);
            }

            fileName = fileUtil.FileUpload(serverPath, file, Code.PROFILE.getCode(), userDto.getUsersId());

        }
        // 비밀번호 암호화
        if(editUserRequestDto.getPassword() != null) {
            editUserRequestDto.setPassword(passwordEncoder.encode(editUserRequestDto.getPassword()));
        }

        UserDto result = userRepository.editUser(editUserRequestDto, fileName, userDto.getUsersId());

        return result;
    }

}
