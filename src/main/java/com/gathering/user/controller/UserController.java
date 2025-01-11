package com.gathering.user.controller;

import com.gathering.common.base.exception.BaseException;
import com.gathering.common.base.response.BaseResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.gathering.service.search.GatheringSearchService;
import com.gathering.user.model.constant.SingUpType;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.*;
import com.gathering.user.model.dto.response.UserAttendanceBookResponse;
import com.gathering.user.redis.UserRedisKey;
import com.gathering.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.YearMonth;
import java.util.List;

import static com.gathering.common.base.response.BaseResponseStatus.*;
import static com.gathering.security.jwt.JwtTokenUtil.createRefreshToken;
import static com.gathering.security.jwt.JwtTokenUtil.generateToken;
import static com.gathering.user.util.Validator.isValidEmail;

@RestController
@RequestMapping("/api/auths")
@RequiredArgsConstructor
@Validated  // 파라미터의 유효성 검사를 활성화
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final GatheringSearchService gatheringSearchService;

    @RequestMapping(value = "/getAccessToken", method = RequestMethod.POST)
    public BaseResponse<String> getAccessToken(
            @RequestBody GetAccessTokenDto getAccessTokenDto
            ){
//        String accessToken = userService.getAccessToken(getAccessTokenDto);
        String accessToken = generateToken(getAccessTokenDto.getUserId());
        return new BaseResponse<>(accessToken);
    }

    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public BaseResponse<UserDto> signIn(
            @Valid @RequestBody SignInRequestDto requestDto,
            HttpServletRequest request,
            HttpServletResponse response) {

        UserDto userDto = userService.sginIn(requestDto);
        // 토큰 발급
        if(userDto != null) {

            // 리프레시 토큰 redis 저장
            userService.setRefreshTokenRedis(UserRedisKey.getUserRefreshTokenKey(request, response), createRefreshToken(userDto.getUserName()));

            return new BaseResponse<>(userDto);
        }

        return new BaseResponse<>(SIGN_IN_FAIL);
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public BaseResponse<Void> signUp(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto  // JSON 데이터
    ) {
        try {
            userService.signUp(signUpRequestDto);
        } catch (Exception e) {
            return new BaseResponse<>(DATABASE_ERROR);
        }

        return new BaseResponse<>();
    }

    @RequestMapping(value = "/check/{type}", method = RequestMethod.GET)
    @Operation(summary = "email/userName 체크", description = "email/userName 체크 " +
            "type이 email일 경우 이메일 체크" +
            "type이 userName일 경우 아이디 체크 결과 반환")
    public BaseResponse<Void> checkType(
            @RequestParam String param,
            @PathVariable String type
    ) {

        boolean typeBol = type.equals(SingUpType.EMAIL.getValue());

        if (param == null || param.isBlank()) {
            return new BaseResponse<>(INVALID_REQUEST);
        }

        // 이메일일 경우 이메일 유효성 검사
        if (typeBol && !isValidEmail(param)) {
            return new BaseResponse<>(INVALID_REQUEST);
        }
        boolean result = userService.checkType(param, typeBol);

        if(result) {
            return new BaseResponse<>();
        }
        return new BaseResponse<>(typeBol ? DUPLICATE_EMAIL : DUPLICATE_USERNAME);
    }

    @RequestMapping(value = "/signOut", method = RequestMethod.POST)
    public BaseResponse<Void> signout(
            HttpServletRequest request, HttpServletResponse response
    ) {

        // 로그아웃 처리
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);  // 로그아웃 처리

        // 필요 시 세션 무효화
        request.getSession().invalidate();

        // 리프레시 토큰 삭제
        userService.deleteRefreshTokenRedis(UserRedisKey.getUserRefreshTokenKey(request, response));

        return new BaseResponse<>();
    }

    @RequestMapping(value = "/myProfile", method = RequestMethod.GET)
    public BaseResponse<UserDto> myprofile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        UserDto userDto = userService.selectUserInfo(userDetails.getUsername());
        
        return new BaseResponse<>(userDto);
    }

    /*
    *  사용자 정보 수정
    *  1. 변경 여부 상관 없이 아이디,이메일 값을 받음 (비밀번호의 경우 변경이 있으면 받고 없으면 받지 않음)
    *  2. 프로필 사진에 변경이 있을경우 file을 받고 없을경우 받지 않음
    *  3. 프로필 사진 변경시 기존 파일 삭제, 테이블 데이터 제거
    *  4. 이후 서베에 파일 저장, 사용자 정보 업데이트
    */
    @RequestMapping(value = "/edit/user", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<UserDto> editUser(
            @RequestPart("data") @Valid EditUserRequestDto editUserRequestDto,   // JSON 데이터
            @RequestPart(value = "file" ,required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails
    )throws RuntimeException {

        UserDto user = null;
        try {
            user = userService.editUser(editUserRequestDto, file, userDetails.getUsername());
            if(user != null) {
                String accessToken = generateToken(user.getUserName());
                user.setToken(accessToken);
            }
            user.setPassword(null);
        } catch (Exception e) {
            throw new BaseException(EDIT_USER_FAIL);
        }

        return new BaseResponse<>(user);
    }

    @RequestMapping(value = "/password/check", method = RequestMethod.POST)
    public BaseResponse<Void> passwordCheck(
            @RequestBody @Valid PasswordCheckDto passwordCheckDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        String username = userDetails.getUsername();

        UserDto userDto = userService.selectUserInfo(username);

        if (passwordEncoder.matches(passwordCheckDto.getPassword(), userDto.getPassword())) {
            return new BaseResponse<>();
        } else {
            return new BaseResponse<>(BaseResponseStatus.PASSWORD_MISMATCHED);
        }

    }
    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public BaseResponse<UserDto> reissueToken(
            HttpServletRequest request, HttpServletResponse response
    ) {

        UserDto userDto = userService.reissueToken(UserRedisKey.getUserRefreshTokenKey(request, response));

        return new BaseResponse<>(userDto);
    }


    /**
     * 달력의 날짜 별 내가 읽었던 책 기록
     */
    @RequestMapping(value = "/myBookCalendar", method = RequestMethod.GET)
    public BaseResponse<List<UserAttendanceBookResponse>> getBooksByCalendarDate(@AuthenticationPrincipal UserDetails userDetails,
                                                                                 @RequestParam YearMonth yearMonth) {
        return new BaseResponse<>(userService.getBooksByCalendarDate(userDetails.getUsername(), yearMonth));
    }
}
