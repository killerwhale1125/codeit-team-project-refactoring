package com.gathering.user.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public record SignInRequestDto(
        @NotNull
        @NotEmpty
        String userName) {
}
