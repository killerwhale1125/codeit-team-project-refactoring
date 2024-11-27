package com.gathering.user.controller;

import com.gathering.common.base.response.BaseResponseStatus;
import com.gathering.user.model.dto.UserDto;
import com.gathering.user.model.entitiy.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.toString;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "사용자 API", description = "사용자 응답 관련 api")
@ApiResponse(responseCode = "200", description = "success")
@ApiResponse(responseCode = "500", description = "failed")
public class UserController {


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @Operation(summary = "회원가입", description = "signup", security = {})
    public String test(
        @RequestBody UserDto userDto
    ) {


        return "test";
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @Operation(summary = "테스트", description = "user test")
    public String test2(
            @RequestBody User user
    ) {


        return "test";
    }
}
