package com.gathering.user.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EditUserRequestDto {


    @NotEmpty
    private String userName;
    private String password;
    @NotEmpty
    private String email;

}
