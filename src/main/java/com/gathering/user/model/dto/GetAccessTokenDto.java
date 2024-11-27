package com.gathering.user.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GetAccessTokenDto {

    @NotNull
    private String userId;
    @NotNull
    private String password;

}
