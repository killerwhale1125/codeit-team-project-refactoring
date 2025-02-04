package com.gathering.user.service;

import com.gathering.common.base.exception.BaseException;
import com.gathering.image.domain.ImageDomain;
import com.gathering.image.service.AwsS3Service;
import com.gathering.image.service.ImageService;
import com.gathering.image.service.port.ImageRepository;
import com.gathering.security.jwt.JwtProviderHolder;
import com.gathering.user.controller.port.UserService;
import com.gathering.user.domain.*;
import com.gathering.user.service.port.UserRepository;
import com.gathering.user.util.EmailValidator;
import com.gathering.user.util.PasswordEncoderHolder;
import com.gathering.user_attendance.domain.UserAttendanceDomain;
import com.gathering.user_attendance.service.port.UserAttendanceRepository;
import com.gathering.user_attendance_book.controller.response.UserAttendanceBookResponse;
import com.gathering.util.date.DateHolder;
import com.gathering.util.image.SystemFileUtils;
import com.gathering.util.string.UUIDUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static com.gathering.common.base.response.BaseResponseStatus.*;
import static com.gathering.image.infrastructure.entity.EntityType.USER;
import static com.gathering.user.domain.SingUpType.EMAIL;

@RequiredArgsConstructor
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderHolder passwordEncoder;
    private final AwsS3Service awsS3Service;
    private final UUIDUtils uuidUtils;
    private final DateHolder dateHolder;
    private final UserAttendanceRepository userAttendanceRepository;
    private final JwtProviderHolder jwtProviderHolder;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public UserResponse login(UserLogin userLogin) {
        UserDomain user = userRepository.selectUserByEmail(userLogin.email()).login(userLogin, passwordEncoder);
        insertAttendance(user.getId()); // 출석 체크
        return UserResponse.fromEntity(user, generateTokenAll(user.getUserName()));
    }

    @Override
    public void signUp(UserSignUp userSignUp) {
        ImageDomain image = imageRepository.save(createDefaultImage()); // 기본 이미지 생성
//        ImageDomain image = imageRepository.save(ImageDomain.create("filePath_" + userSignUp.getEmail(), "fileName_" + userSignUp.getEmail()));
        userRepository.save(UserDomain.signUp(userSignUp, image, passwordEncoder));
    }

    @Override
    public UserResponse findByUsername(String username) {
        return UserResponse.fromEntity(userRepository.findByUsername(username));
    }

    @Override
    public UserResponse update(UserUpdate userUpdate, MultipartFile file, String username) {
        UserDomain user = userRepository.findByUsernameWithImage(username);
        ImageDomain image = file.isEmpty() ? user.getImage(): uploadImage(file, user.getProfile());
        user = user.update(userUpdate, image, passwordEncoder);
        
        UserDomain saveUser = userRepository.save(user);
        String accessToken = generateTokenAll(username); // 유저 이름이 JWT 키값이기 때문에 토큰 정보도 초기화
        
        return UserResponse.fromEntity(saveUser, accessToken);
    }

    @Override
    public List<UserAttendanceBookResponse> getBooksByCalendarDate(String username, YearMonth yearMonth) {
//        UserDomain user = userRepository.findByUsername(username);
//
//        // yyyy-mm 으로 받은 것 중 시작일과 종료일 계산
//        LocalDate startDate = dateCalculateUtils.getStartOfMonth(yearMonth);
//        LocalDate endDate = dateCalculateUtils.getEndOfMonth(yearMonth);
//
//        // 시작일 종료일 기준으로 출석 엔티티 조회
//        return userRepository.getUserAttendancesByUserIdAndDate(user.getId(), startDate, endDate).stream()
//                .map(UserAttendanceBookResponse::fromEntity)
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public UserResponse reissueToken(String refreshToken) {
        /**
         * Refresh 토큰 검증
         * 토큰 값이 존재하는데 검증 실패 시 재 로그인 필요
         */
        String username = jwtProviderHolder.extractUsername(refreshToken);

        /* 레디스에 저장되어있는 리프레시토큰을 조회한다. */
        String oldRefreshToken = jwtProviderHolder.getRefreshToken(username).orElseThrow(() -> new BaseException(TOKEN_ISEMPTY));

        /* 값이 비어있거나 요청받은 리프레시토큰과 일치하지 않을 경우 예외를 발생시킨다. */
        if(oldRefreshToken.isBlank() || !refreshToken.equals(oldRefreshToken)) {
            throw new BaseException(TOKEN_MISMATCHED);
        }

        /* 기존 리프레시 토큰은 제거한다. */
        jwtProviderHolder.deleteRefreshToken(username);

        UserDomain user = userRepository.findByUsername(username);
        return UserResponse.fromEntity(user, generateTokenAll(username));
    }

    @Override
    public void verifyUsernameOrEmail(String param, SingUpType type) {
        // 이메일일 경우 이메일 유효성 검사
        if (type.equals(EMAIL) && !EmailValidator.isValidEmail(param)) {
            throw new BaseException(INVALID_REQUEST);
        }

        if (!userRepository.checkType(param, type)) {
            switch (type) {
                case EMAIL -> throw new BaseException(DUPLICATE_EMAIL);
                case ID -> throw new BaseException(DUPLICATE_USERNAME);
            }
        }
    }

    @Override
    public void verifyPassword(PasswordCheck passwordCheck, String username) {
        UserDomain user = userRepository.findByUsername(username);

        if (!passwordEncoder.verifyPassword(passwordCheck.getPassword(), user.getPassword())) {
            throw new BaseException(PASSWORD_MISMATCHED);
        }
    }

    private ImageDomain createDefaultImage() {
        return imageService.uploadImage(new ArrayList<>(), USER, uuidUtils).get(0);
    }

    private ImageDomain uploadImage(MultipartFile file, String profile) {
        try {
            String filename = SystemFileUtils.getRandomFilename(uuidUtils);
            String filePath = awsS3Service.upload(file, filename, USER);
            return ImageDomain.create(filePath, filename);
        } catch(Exception e) {
            throw new BaseException(FILE_UPLOAD_FAILED);
        } finally {
            try {
                if(!profile.isBlank()) awsS3Service.delete(profile);
            } catch (Exception e) {
                throw new BaseException(FILE_DELETE_FAILED);
            }
        }
    }

    private String generateTokenAll(String username) {
        String accessToken = jwtProviderHolder.createAccessToken(username); // access 토큰 생성
        jwtProviderHolder.saveRefreshToken(username);   // refresh 토큰 생성 후 Redis 저장
        return accessToken;
    }

    private void insertAttendance(Long userId) {
        LocalDate today = dateHolder.localDateNow();
        UserAttendanceDomain userAttendance = userAttendanceRepository.findByUserIdAndCreateDate(userId, today);
        if(userAttendance == null) {
            userAttendanceRepository.insert(userId);
        }
    }
}
