package com.gathering.user.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.security.jwt.JwtProviderHolder;
import com.gathering.user.controller.port.UserService;
import com.gathering.user.domain.*;
import com.gathering.user_attendance_book.controller.response.UserAttendanceBookResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.YearMonth;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/auths")
@RequiredArgsConstructor
@Validated  // 파라미터의 유효성 검사를 활성화
public class UserController {

    private final UserService userService;
    private final JwtProviderHolder jwtProviderHolder;

    @PostMapping("/signUp")
    public BaseResponse<Void> signUp(@Valid @RequestBody UserSignUp userSignUp) {
        userService.signUp(userSignUp);
        return new BaseResponse<>();
    }

    @PostMapping("/login")
    public BaseResponse<UserResponse> login(@Valid @RequestBody UserLogin userLogin) {
        return new BaseResponse<>(userService.login(userLogin));
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request, HttpServletResponse response) {
        /* Security 로그아웃 */
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);  // 로그아웃 처리

        /* refresh 토큰 삭제 */
        jwtProviderHolder.deleteRefreshToken(userDetails.getUsername());
        return new BaseResponse<>();
    }

    @RequestMapping(value = "/myProfile", method = RequestMethod.GET)
    public BaseResponse<UserResponse> myprofile(@AuthenticationPrincipal UserDetails userDetails) {
        return new BaseResponse<>(userService.findByUsername(userDetails.getUsername()));
    }

    @PutMapping("/update")
    public BaseResponse<UserResponse> update(
            @RequestPart("data") @Valid UserUpdate userUpdate,
             @RequestPart(value = "file" ,required = false) MultipartFile file,
             @AuthenticationPrincipal UserDetails userDetails) {
        return new BaseResponse<>(userService.update(userUpdate, file, userDetails.getUsername()));
    }

    @GetMapping("/verify/{type}")
    public BaseResponse<Void> verifyUsernameOrEmail(@RequestParam String param,
                                                    @PathVariable SingUpType type) {
        userService.verifyUsernameOrEmail(param, type);
        return new BaseResponse<>();
    }

    @RequestMapping(value = "/password/check", method = RequestMethod.POST)
    public BaseResponse<Void> verifyPassword(
            @RequestBody @Valid PasswordCheck passwordCheck,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.verifyPassword(passwordCheck, userDetails.getUsername());
        return new BaseResponse<>();
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public BaseResponse<UserResponse> reissueToken(HttpServletRequest request) {
        String refreshToken = jwtProviderHolder.resolveToken(request.getHeader(AUTHORIZATION));
        return new BaseResponse<>(userService.reissueToken(refreshToken));
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
