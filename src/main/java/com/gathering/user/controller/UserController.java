package com.gathering.user.controller;

import com.gathering.common.base.response.BaseResponse;
import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.user.model.constant.SingUpType;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.dto.request.EditUserRequestDto;
import com.gathering.user.model.dto.request.GetAccessTokenDto;
import com.gathering.user.model.dto.request.SignInRequestDto;
import com.gathering.user.model.dto.request.SignUpRequestDto;
import com.gathering.user.service.UserService;
import com.gathering.user.util.Validator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.gathering.security.jwt.JwtTokenUtil.generateToken;

@RestController
@RequestMapping("/api/auths")
@RequiredArgsConstructor
@Tag(name = "사용자 API", description = "사용자 응답 관련 api")
@ApiResponse(responseCode = "200", description = "success")
@Validated  // 파라미터의 유효성 검사를 활성화
public class UserController {


    private final UserService userService;

    @RequestMapping(value = "/getAccessToken", method = RequestMethod.POST)
    @Operation(summary = "토큰 발급(임시)", description = "개발용 토큰 발급 api")
    public BaseResponse<String> getAccessToken(
            @RequestBody GetAccessTokenDto getAccessTokenDto
            ){
//        String accessToken = userService.getAccessToken(getAccessTokenDto);
        String accessToken = generateToken(getAccessTokenDto.getUserId());
        return new BaseResponse<>(accessToken);
    }

    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    @Operation(summary = "로그인", description = "사용자 로그인")
    public BaseResponse<UserDto> signIn(
            @Valid @RequestBody SignInRequestDto requestDto) {

        UserDto userDto = userService.sginIn(requestDto);
        // 토큰 발급
        if(userDto != null) {
            GetAccessTokenDto getAccessTokenDto= new GetAccessTokenDto();
            getAccessTokenDto.setUserId(requestDto.userName());
            getAccessTokenDto.setPassword(requestDto.password());
            String accessToken = generateToken(getAccessTokenDto.getUserId());

            userDto.setToken(accessToken);
            return new BaseResponse<>(userDto);
        }

        return new BaseResponse<>(BaseResponseStatus.AUTHORIZATION_FAIL);
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    @Operation(summary = "회원가입", description = "signUp")
    public BaseResponse<Void> signUp(
            @Valid @RequestBody SignUpRequestDto signUpRequestDto  // JSON 데이터
    ) {
        try {
            userService.signUp(signUpRequestDto);
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.DATABASE_ERROR);
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
            return new BaseResponse<>(BaseResponseStatus.INVALID_REQUEST);
        }

        // 이메일일 경우 이메일 유효성 검사
        if (typeBol && !Validator.isValidEmail(param)) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_REQUEST);
        }
        boolean result = userService.checkType(param, typeBol);

        if(result) {
            return new BaseResponse<>();
        }
        return new BaseResponse<>(typeBol ? BaseResponseStatus.DUPLICATE_EMAIL : BaseResponseStatus.DUPLICATE_USERNAME);
    }

    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    @Operation(summary = "사용자 로그아웃", description = "사용자 로그아웃 ")
    public BaseResponse<Void> signout(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request, HttpServletResponse response
    ) {

        // 로그아웃 처리
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);  // 로그아웃 처리

        // 필요 시 세션 무효화
        request.getSession().invalidate();
        return new BaseResponse<>();
    }

    /*
    * TODO - 참여 모임 , 작성한 리뷰 등 모임 관련 내용 추가 핋요
    * */
    @RequestMapping(value = "/myprofile", method = RequestMethod.GET)
    @Operation(summary = "마이 프로필", description = "사용자 정보 제공")
    public BaseResponse<Void> myprofile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        UserDto userDto = userService.selectUserInfo(userDetails.getUsername());
        
        return new BaseResponse<>();
    }

    /*
    *  사용자 정보 수정
    *  1. 변경 여부 상관 없이 아이디,이메일 값을 받음 (비밀번호의 경우 변경이 있으면 받고 없으면 받지 않음)
    *  2. 프로필 사진에 변경이 있을경우 file을 받고 없을경우 받지 않음
    *  3. 프로필 사진 변경시 기존 파일 삭제, 테이블 데이터 제거
    *  4. 이후 서베에 파일 저장, 사용자 정보 업데이트
    */
    @RequestMapping(value = "/edit/user", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정<br>" +
            "비밀번호의 경우 변경시 받고 없으면 제외하고 전송하면 됩니다.<br>" +
            "변경여부 상관없이 아이디랑 이메일 값을 전송 하시면 됩니다.<br>")
    public BaseResponse<UserDto> editUser(
            @RequestPart("data") @Valid EditUserRequestDto editUserRequestDto,   // JSON 데이터
            @RequestPart(value = "file" ,required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails
    )throws RuntimeException {

        UserDto user = null;
        try {
            user = userService.editUser(editUserRequestDto, file, userDetails.getUsername());
            user.setPassword(null);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse<>(BaseResponseStatus.EDIT_USER_FAIL);
        }

        return new BaseResponse<>(user);
    }
}
