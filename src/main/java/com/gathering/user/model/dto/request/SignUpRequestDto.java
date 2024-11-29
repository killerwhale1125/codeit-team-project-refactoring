package com.gathering.user.model.dto.request;

import com.gathering.common.base.jpa.BaseTimeEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignUpRequestDto extends BaseTimeEntity {

    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;
    @NotEmpty
    private String email;
    private String city;
    private String state;
    private String town;

}
