package com.gathering.user.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SignInRequestDto(
        @NotNull
        @NotEmpty
        String userName) {
}
