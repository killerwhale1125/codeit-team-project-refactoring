package com.gathering.user.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SignInRequestDto(
        @Email
        @NotEmpty
        String email,

        @NotNull
        @NotEmpty
        String password
) {
}
