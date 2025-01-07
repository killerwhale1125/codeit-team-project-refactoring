package com.gathering.user.service;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.image.model.entity.EntityType;
import com.gathering.image.service.AwsS3Service;
import com.gathering.security.jwt.JwtTokenUtil;
import com.gathering.user.model.domain.UserDomain;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.EditUserRequestDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user.model.dto.response.UserAttendanceBookResponse;
import com.gathering.user.repository.UserRepository;
import com.gathering.util.date.DateCalculateUtils;
import com.gathering.util.image.SystemFileUtils;
import com.gathering.util.string.UUIDUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.gathering.security.jwt.JwtTokenUtil.generateToken;

@RequiredArgsConstructor
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DateCalculateUtils dateCalculateUtils;
    private final AwsS3Service awsS3Service;
    private final RedisTemplate<String, String> redisTemplate;
    private final UUIDUtils uuidUtils;

    @Override
    public UserDto sginIn(SignInRequestDto requestDto) {

        UserDto userDto = userRepository.selectUserByEmail(requestDto.email());

        if (userDto == null) {
            return null;
        } else if(passwordEncoder.matches(requestDto.password(), userDto.getPassword())) {
            userRepository.insertAttendance(userDto.getUsersId());
            userDto.setPassword(null);

            String accessToken = generateToken(userDto.getUserName());
            userDto.setToken(accessToken);

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
        String filepath = "";
        UserDto userDto = userRepository.selectUser(userName);
        if( file != null) {
            String profile = userDto.getProfile();
            try {
                // 기존 프로필 파일 삭제
                if(profile != null && !profile.isEmpty()) {
                        awsS3Service.delete(profile);
                    }
                String filename = SystemFileUtils.getRandomFilename(uuidUtils);
                filepath = awsS3Service.upload(file, filename, EntityType.USER);

            } catch (Exception e) {
                // 로깅 및 예외 처리
                log.error("Failed to delete image from S3: " + profile, e);
            }
        }
        // 비밀번호 암호화
        if(editUserRequestDto.getPassword() != null) {
            editUserRequestDto.setPassword(passwordEncoder.encode(editUserRequestDto.getPassword()));
        }

        UserDto result = userRepository.editUser(editUserRequestDto, filepath, userDto.getUsersId());

        return result;
    }

    @Override
    public List<UserAttendanceBookResponse> getBooksByCalendarDate(String username, YearMonth yearMonth) {
        UserDomain user = userRepository.findByUsername(username);
        
        // yyyy-mm 으로 받은 것 중 시작일과 종료일 계산
        LocalDate startDate = dateCalculateUtils.getStartOfMonth(yearMonth);
        LocalDate endDate = dateCalculateUtils.getEndOfMonth(yearMonth);
        
        // 시작일 종료일 기준으로 출석 엔티티 조회
        return userRepository.getUserAttendancesByUserIdAndDate(user.getId(), startDate, endDate).stream()
                .map(UserAttendanceBookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void setRefreshTokenRedis(String redisKey, String token) {

        if(Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            redisTemplate.delete(redisKey);
        }

        // redis에 7일동안 리프레시 토큰 저장
        redisTemplate.opsForValue().set(redisKey, token,7, TimeUnit.DAYS);

    }

    @Override
    public void deleteRefreshTokenRedis(String redisKey) {
        redisTemplate.delete(redisKey);
    }

    @Override
    public UserDto reissueToken(String redisKey) {

        String oldRefreshToken = redisTemplate.opsForValue().get(redisKey);

        if(oldRefreshToken != null) {
            try{
                // 서버 재기동 이후 리프레시 토큰을 가져올 경우 인증 방식이 달라지기때문에 에러 발생
                // 레디스의 리프레시 토큰 제거
                String username = JwtTokenUtil.extractUsername(oldRefreshToken);

                UserDto userDto = userRepository.selectUser(username);

                userDto.setPassword(null);
                String accessToken = generateToken(userDto.getUserName());
                userDto.setToken(accessToken);

                // 기존 refresh token 삭제 후 새로운 refresh토큰으로 재설정
                redisTemplate.delete(redisKey);
                setRefreshTokenRedis(redisKey, JwtTokenUtil.createRefreshToken(username));

                return userDto;
            } catch (Exception e) {
                redisTemplate.delete(redisKey);
                throw new BaseException(BaseResponseStatus.INVALID_TOKEN);
            }
        } else {
            throw new BaseException(BaseResponseStatus.REFRESH_TOKEN_ISEMPTY);
        }
    }

}
