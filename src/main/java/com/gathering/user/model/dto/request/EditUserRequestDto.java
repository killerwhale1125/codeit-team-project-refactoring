package com.gathering.user.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EditUserRequestDto {


    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{1,10}$")
    private String userName;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[a-zA-Z\\d@$!%*?&]{8,}$")
    private String password;

    @NotEmpty
    @Email
    private String email;

}